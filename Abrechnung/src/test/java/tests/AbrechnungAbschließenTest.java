package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.junit.After;
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
import boundingContext.abrechnung.aufz‰hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz‰hlungen.SachKonto;
import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.helper.AbrechnungAbschlieﬂen;
import boundingContext.abrechnung.helper.AbrechnungHelper;
import boundingContext.abrechnung.helper.SaldoAusgleichen;
import boundingContext.abrechnung.helper.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.‹berweisungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsB¸ndelMap;
import boundingContext.zahlungen.helper.ZahlungsAuftragsManager;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class AbrechnungAbschlieﬂenTest {

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

    @Autowired
    private ZahlungsDefinitionRepository zahlungsDefinitionRepository;

    @Autowired
    private ‹berweisungRepository ¸berweisungRepository;

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(double betrag) {
        BetragsB¸ndelMap<SachKonto> betr‰ge = new BetragsB¸ndelMap<>();
        betr‰ge.put(TestSachKonto.BETRAG, Geld.createAmount(betrag));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<SachKonto>(beschreibung, betr‰ge);
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
        return new EinBucher(sachKontoProvider(), buchungRepository,
                kontoBewegungRepository, abrechnungRepository);
    }

    @Test
    @Transactional("dbATransactionManager")
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
    public void schulden‹benehmen360() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung n‰chsteAbrechnung = schulden‹bernahme(abrechnung, 0.06, 360);
        checkAnzahlen();
        check‹bernahme(n‰chsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schulden‹benehmen180() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung n‰chsteAbrechnung = schulden‹bernahme(abrechnung, 0.06, 180);
        checkAnzahlen();
        check‹bernahme(n‰chsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, -100);
        Abrechnung n‰chsteAbrechnung = abschlieﬂen(abrechnung);
        checkAnzahlen();
        check‹bernahme(n‰chsteAbrechnung, -100, -3);
    }

    private Abrechnung abschlieﬂen(Abrechnung abrechnung) {
        AbrechnungAbschlieﬂen abchluss = new AbrechnungAbschlieﬂen(
                sachKontoProvider(), buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                zahlungsAuftragRepository, 0.06);
        Abrechnung n‰chsteAbrechnung = abchluss.abschleiﬂen(abrechnung, 180);
        return n‰chsteAbrechnung;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);

        Abrechnung n‰chsteAbrechnung = abschlieﬂen(abrechnung);
        check‹bernahmeOhneWirkung(n‰chsteAbrechnung);
    }

    public Abrechnung saldoAbgleichen(double betrag) {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, betrag);

        SaldoAusgleichen abschluss = new SaldoAusgleichen(sachKontoProvider(),
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository,
                "Guthaben", "Schulden");
        abschluss.saldoAusgleichen(abrechnung);
        return abrechnung;
    }

    private void erzeugeBuchung(Abrechnung abrechnung, double betrag) {
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(betrag);
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeBuchung(auftrag, abrechnung);
    }

    public void checkAbgleich(Abrechnung abrechnung, int art,
            SachKonto kontonr, double betrag) {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        for (Buchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }

        checkKontoBetrag(abrechnung, art, kontonr, betrag);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo.isZero());

    }

    private void checkKontoBetrag(Abrechnung abrechnung, int art,
            SachKonto kontonr, double betrag) {
        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                art, kontonr.ordinal());
        if (dbBetrag == null) {
            dbBetrag = Geld.getNull();
        }
        assertEquals(Geld.createAmount(betrag), dbBetrag);
    }

    public Abrechnung schulden‹bernahme(Abrechnung abrechnung, double zinssatz,
            int tage) {
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Abrechnung n‰chsteAbrechnung = helper
                .createOrGetN‰chsteAbrechnung(abrechnung);

        SchuldenInDieAbrechnung ¸bernehmen = new SchuldenInDieAbrechnung(
                sachKontoProvider(), buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                "Schulden ¸bernehmen", zinssatz);
        ¸bernehmen.¸bertragen(n‰chsteAbrechnung, tage);
        return n‰chsteAbrechnung;
    }

    public void check‹bernahme(Abrechnung abrechnung, double betrag, double zins) {

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        assertEquals(Geld.createAmount(betrag), dbBetrag);
        assertEquals(Geld.createAmount(zins), dbZins);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertEquals(Geld.createAmount(betrag + zins), saldo);

    }

    private void checkAnzahlen() {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(3, buchungRepository.count());
    }

    public void check‹bernahmeOhneWirkung(Abrechnung abrechnung) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        assertTrue(dbBetrag == null || dbBetrag.isZero());
        assertTrue(dbZins == null || dbZins.isZero());
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo == null || saldo.isZero());

    }

    @Test
    @Transactional("dbATransactionManager")
    public void mehrmaligerAbschluss() {
        Mandant mandant = erzeugeMandant();
        mandant = addZahlungsDefinition(mandant, 0.3);
        mandant = addZahlungsDefinition(mandant, 0.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);
        Abrechnung n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        ZahlungsAuftragsManager zahlungenManager = new ZahlungsAuftragsManager(
                new TestSachKontoProvider(), buchungRepository,
                kontoBewegungRepository, zahlungsAuftragRepository,
                ¸berweisungRepository, abrechnungRepository);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -100);
        checkKontoBetrag(abrechnung, BuchungsArt.‹BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, -40);
        n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -60);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, -60);
        n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, 20);
        n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        zahlungenManager.erzeugeAuftr‰ge(abrechnung, Geld.createAmount(20),
                "Test");

        erzeugeBuchung(abrechnung, -40);
        n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 20);
        checkKontoBetrag(n‰chsteAbrechnung, BuchungsArt.‹BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, -20);

        erzeugeBuchung(abrechnung, -40);
        n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 60);
        checkKontoBetrag(n‰chsteAbrechnung, BuchungsArt.‹BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, -60);

        erzeugeBuchung(abrechnung, 80);
        n‰chsteAbrechnung = abschlieﬂen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);
        checkKontoBetrag(n‰chsteAbrechnung, BuchungsArt.‹BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);
        // System.out.println("Test Ende");
    }

    private Mandant addZahlungsDefinition(Mandant mandant, double prozentSatz) {
        ZahlungsDefinition d = new ZahlungsDefinition();
        d.setBuchungsart(1);
        d.setBank(new BankVerbindung(new IBAN("123"), new BIC("999")));
        d.setProzentSatz(prozentSatz);
        d.setTag(1);
        d.setMandant(mandant);
        mandant.addZahlungsDefinitionen(d);
        zahlungsDefinitionRepository.save(d);
        return mandantRepository.save(mandant);
    }

}
