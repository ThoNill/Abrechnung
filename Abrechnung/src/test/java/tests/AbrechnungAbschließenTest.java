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
import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.abrechnung.helper.AbrechnungAbschließen;
import boundingContext.abrechnung.helper.AbrechnungHelper;
import boundingContext.abrechnung.helper.SaldoAusgleichen;
import boundingContext.abrechnung.helper.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;
import boundingContext.zahlungen.helper.ZahlungsAuftragsManager;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;

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

    @Autowired
    private ZahlungsDefinitionRepository zahlungsDefinitionRepository;

    @Autowired
    private ÜberweisungRepository überweisungRepository;

    
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
        return new EinBucher(sachKontoProvider(), buchungRepository,
                kontoBewegungRepository,abrechnungRepository);
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
    public void schuldenÜbenehmen360() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06, 360);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schuldenÜbenehmen180() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06, 180);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung,-100);
        Abrechnung nächsteAbrechnung = abschließen(abrechnung);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    private Abrechnung abschließen(Abrechnung abrechnung) {
        AbrechnungAbschließen abchluss = new AbrechnungAbschließen(
                sachKontoProvider(), buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                zahlungsAuftragRepository, 0.06);
        Abrechnung nächsteAbrechnung = abchluss.abschleißen(abrechnung, 180);
        return nächsteAbrechnung;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung,100);

        Abrechnung nächsteAbrechnung = abschließen(abrechnung);
        checkÜbernahmeOhneWirkung(nächsteAbrechnung);
    }

    public Abrechnung saldoAbgleichen(double betrag) {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung,betrag);

        SaldoAusgleichen abschluss = new SaldoAusgleichen(sachKontoProvider(),
                buchungRepository, kontoBewegungRepository,abrechnungRepository,
                BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,
                "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, "Schulden");
        abschluss.saldoAusgleichen(abrechnung);
        return abrechnung;
    }

    private void erzeugeBuchung(Abrechnung abrechnung,double betrag) {
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

    public Abrechnung schuldenÜbernahme(Abrechnung abrechnung, double zinssatz,
            int tage) {
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Abrechnung nächsteAbrechnung = helper
                .createOrGetNächsteAbrechnung(abrechnung);

        SchuldenInDieAbrechnung übernehmen = new SchuldenInDieAbrechnung(
                sachKontoProvider(), buchungRepository,
                kontoBewegungRepository, abrechnungRepository,
                BuchungsArt.ABGLEICH_SCHULDEN, BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, TestSachKonto.ZINS,
                "Schulden übernehmen", zinssatz);
        übernehmen.übertragen(nächsteAbrechnung, tage);
        return nächsteAbrechnung;
    }

    public void checkÜbernahme(Abrechnung abrechnung, double betrag, double zins) {
       
        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
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

    public void checkÜbernahmeOhneWirkung(Abrechnung abrechnung) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        assertTrue(dbBetrag == null || dbBetrag.isZero());
        assertTrue(dbZins == null || dbZins.isZero());
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo == null || saldo.isZero());

    }

    @Test    
    @Transactional("dbATransactionManager")
    public void mehrmaligerAbschluss() {
        Mandant mandant = erzeugeMandant();
        mandant = addZahlungsDefinition(mandant,0.3);
        mandant = addZahlungsDefinition(mandant,0.7);
        
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung,100);
        Abrechnung nächsteAbrechnung = abschließen(abrechnung);
        
        ZahlungsAuftragsManager zahlungenManager = new ZahlungsAuftragsManager(new TestSachKontoProvider(),
                buchungRepository,
                kontoBewegungRepository,
                zahlungsAuftragRepository,
                überweisungRepository,
                abrechnungRepository,TestSachKonto.GUTHABEN,
                TestSachKonto.AUSBEZAHLT);
        
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,-100);
        checkKontoBetrag(abrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN,0);
        
        erzeugeBuchung(abrechnung,-40);
        nächsteAbrechnung = abschließen(abrechnung);
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,-60);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN, TestSachKonto.SCHULDEN,0);
        
        erzeugeBuchung(abrechnung,-60);
        nächsteAbrechnung = abschließen(abrechnung);
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN, TestSachKonto.SCHULDEN,0);
        
        erzeugeBuchung(abrechnung,20);
        nächsteAbrechnung = abschließen(abrechnung);
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,-20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN, TestSachKonto.SCHULDEN,0);
    
        zahlungenManager.erzeugeAufträge(abrechnung,Geld.createAmount(20), "Test");
        
        erzeugeBuchung(abrechnung,-40);
        nächsteAbrechnung = abschließen(abrechnung);
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN, TestSachKonto.SCHULDEN,20);
        checkKontoBetrag(nächsteAbrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN,-20);
        
        erzeugeBuchung(abrechnung,-40);
        nächsteAbrechnung = abschließen(abrechnung);
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN, TestSachKonto.SCHULDEN,60);
        checkKontoBetrag(nächsteAbrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN,-60);
        
        erzeugeBuchung(abrechnung,80);
        nächsteAbrechnung = abschließen(abrechnung);
        
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN, TestSachKonto.GUTHABEN,-20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN, TestSachKonto.SCHULDEN,0);
        checkKontoBetrag(nächsteAbrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.SCHULDEN,0);
//        System.out.println("Test Ende");
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
