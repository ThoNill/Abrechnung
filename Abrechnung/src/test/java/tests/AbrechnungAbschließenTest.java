package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tests.konten.TestSachKonto;
import betrag.Geld;
import boundingContext.abrechnung.actions.SaldoAusgleichen;
import boundingContext.abrechnung.actions.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.ZahlungsDefinition;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündelMap;
import boundingContext.zahlungen.actions.ZahlungsAufträgeErzeugen;
import boundingContext.zahlungen.values.BIC;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.IBAN;
import boundingContext.zahlungen.values.MonatJahr;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class AbrechnungAbschließenTest extends AbrechnungBasisTest {

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
        abrechnung.setMj(new MonatJahr(4, 2018));

        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung = abrechnungRepository.save(abrechnung);
        mandant.addAbrechnung(abrechnung);
        return abrechnung;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider());
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
        Abrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06,
                0.19, 360);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schuldenÜbenehmen180() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06,
                0.19, 180);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, -100);
        Abrechnung nächsteAbrechnung = abschließen(abrechnung);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    private Abrechnung abschließen(Abrechnung abrechnung) {
        Abrechnung nächsteAbrechnung = abrechnung.abschleißen(
                sachKontoProvider(), 180, 0.06, 0.19);
        return nächsteAbrechnung;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);

        Abrechnung nächsteAbrechnung = abschließen(abrechnung);
        checkÜbernahmeOhneWirkung(nächsteAbrechnung);
    }

    public Abrechnung saldoAbgleichen(double betrag) {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, betrag);

        SaldoAusgleichen abschluss = new SaldoAusgleichen(sachKontoProvider(),
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

    public Abrechnung schuldenÜbernahme(Abrechnung abrechnung, double zinssatz,
            double mwstsatz, int tage) {
        SachKontoProvider provider = sachKontoProvider();
        Abrechnung nächsteAbrechnung = abrechnung
                .createOrGetNächsteAbrechnung(provider);

        SchuldenInDieAbrechnung übernehmen = new SchuldenInDieAbrechnung(
                sachKontoProvider(), "Schulden übernehmen", zinssatz, mwstsatz);
        übernehmen.übertragen(nächsteAbrechnung, tage);
        return nächsteAbrechnung;
    }

    public void checkÜbernahme(Abrechnung abrechnung, double betrag, double zins) {

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        MonetaryAmount dbMwst = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.ÜBERNAHME_SCHULDEN, TestSachKonto.MWST.ordinal());
        double mwst = 0.19 * zins;
        assertEquals(Geld.createAmount(betrag), dbBetrag);
        assertEquals(Geld.createAmount(zins), dbZins);
        assertEquals(Geld.createAmount(mwst), dbMwst);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertEquals(Geld.createAmount(betrag + zins + mwst), saldo);

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
        mandant = addZahlungsDefinition(mandant, 0.3);
        mandant = addZahlungsDefinition(mandant, 0.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);
        Abrechnung nächsteAbrechnung = abschließen(abrechnung);

        ZahlungsAufträgeErzeugen zahlungenManager = new ZahlungsAufträgeErzeugen(
                sachKontoProvider());

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -100);
        checkKontoBetrag(abrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, -40);
        nächsteAbrechnung = abschließen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -60);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, -60);
        nächsteAbrechnung = abschließen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, 20);
        nächsteAbrechnung = abschließen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        zahlungenManager.erzeugeAufträge(abrechnung, Geld.createAmount(20),
                "Test");

        erzeugeBuchung(abrechnung, -40);
        nächsteAbrechnung = abschließen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 20);
        checkKontoBetrag(nächsteAbrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, -20);

        erzeugeBuchung(abrechnung, -40);
        nächsteAbrechnung = abschließen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 60);
        checkKontoBetrag(nächsteAbrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, -60);

        erzeugeBuchung(abrechnung, 80);
        nächsteAbrechnung = abschließen(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);
        checkKontoBetrag(nächsteAbrechnung, BuchungsArt.ÜBERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);
    }

    private Mandant addZahlungsDefinition(Mandant mandant, double prozentSatz) {
        ZahlungsDefinition d = new ZahlungsDefinition();
        d.setBuchungsart(1);
        d.setBank(new BankVerbindung(new IBAN("123"), new BIC("999")));
        d.setProzentSatz(prozentSatz);
        d.setTag(1);
        d.setMandant(mandant);
        mandant.addZahlungsDefinitionen(d);
        return mandantRepository.save(mandant);
    }

}
