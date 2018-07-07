package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tests.konten.TestSachKonto;
import tests.konten.TestSachKontoProvider;
import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.helper.AbrechnungAbschließen;
import boundingContext.abrechnung.helper.AbrechnungHelper;
import boundingContext.abrechnung.helper.SaldoAusgleichen;
import boundingContext.abrechnung.helper.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class AbrechnungAbschließenTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Autowired
    private KontoBewegungRepository kontoBewegungRepository;

    @Autowired
    private ZahlungsAuftragRepository zahlungsAuftragRepository;

    
    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(double betrag) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(TestSachKonto.BETRAG, Geld.createAmount(betrag));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<SachKonto>(beschreibung, beträge);
    }

    public Mandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    public Abrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setMandant(mandant);
        abrechnung.setNummer(3);
        abrechnung.setJahr(2018);
        abrechnung.setMonat(4);
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung = abrechnungRepository.save(abrechnung);
        mandant.addAbrechnung(abrechnung);
        return abrechnung;
    }

    private SachKontoProvider sachKontoProvider() {
        return new TestSachKontoProvider();
    }
    
    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider(),buchungRepository, kontoBewegungRepository);
    }

    @Test
    public void ausgleichGuthaben() {
        checkAbgleich(saldoAbgleichen(1.24), BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -1.24);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ausgleichSchulden() {
        checkAbgleich(saldoAbgleichen(-1.24), BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 1.24);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schuldenÜbenehmen360() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06, 360);
        checkÜbernahme(nächsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schuldenÜbenehmen180() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06, 180);
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(-100);
        EinBucher bucher = erzeugeEinbucher();
        bucher.erzeugeBuchung(auftrag, abrechnung);

        AbrechnungAbschließen abchluss = new AbrechnungAbschließen(sachKontoProvider(),
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository,zahlungsAuftragRepository, 0.06);
        Abrechnung nächsteAbrechnung = abchluss.abschleißen(abrechnung, 180);
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(100);
        EinBucher bucher = erzeugeEinbucher();
        bucher.erzeugeBuchung(auftrag, abrechnung);

        AbrechnungAbschließen abchluss = new AbrechnungAbschließen(sachKontoProvider(),
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository,zahlungsAuftragRepository, 0.06);
        Abrechnung nächsteAbrechnung = abchluss.abschleißen(abrechnung, 180);
        checkÜbernahmeOhneWirkung(nächsteAbrechnung);
    }

    @Transactional("dbATransactionManager")
    public Abrechnung saldoAbgleichen(double betrag) {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(betrag);
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeBuchung(auftrag, abrechnung);

        SaldoAusgleichen abschluss = new SaldoAusgleichen(sachKontoProvider(),buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, "Schulden");
        abschluss.saldoAusgleichen(abrechnung);
        return abrechnung;
    }

    @Transactional("dbATransactionManager")
    public void checkAbgleich(Abrechnung abrechnung, int art, SachKonto kontonr,
            double betrag) {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        for (Buchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                art, kontonr.ordinal());
        assertEquals(Geld.createAmount(betrag), dbBetrag);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo.isZero());

    }

    @Transactional("dbATransactionManager")
    public Abrechnung schuldenÜbernahme(Abrechnung abrechnung, double zinssatz,
            int tage) {
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Abrechnung nächsteAbrechnung = helper
                .createOrGetNächsteAbrechnung(abrechnung);

        SchuldenInDieAbrechnung übernehmen = new SchuldenInDieAbrechnung(sachKontoProvider(),
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, BuchungsArt.ABGLEICH_SCHULDEN,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN,
                TestSachKonto.ZINS, "Schulden übernehmen", zinssatz);
        übernehmen.übertragen(nächsteAbrechnung, tage);
        return nächsteAbrechnung;
    }

    @Transactional("dbATransactionManager")
    public void checkÜbernahme(Abrechnung abrechnung, double betrag, double zins) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(3, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        assertEquals(Geld.createAmount(betrag), dbBetrag);
        assertEquals(Geld.createAmount(zins), dbZins);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertEquals(Geld.createAmount(betrag + zins), saldo);

    }

    @Transactional("dbATransactionManager")
    public void checkÜbernahmeOhneWirkung(Abrechnung abrechnung) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        assertTrue(dbBetrag == null || dbBetrag.isZero());
        assertTrue(dbZins == null || dbZins.isZero());
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo == null || saldo.isZero());

    }

}
