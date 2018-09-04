package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.actions.SaldoAusgleichen;
import org.nill.abrechnung.actions.SchuldenInDieAbrechnung;
import org.nill.abrechnung.aufzählungen.BuchungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.tests.konten.TestSachKonto;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.zahlungen.actions.ZahlungsAufträgeErzeugen;
import org.nill.zahlungen.values.BIC;
import org.nill.zahlungen.values.BankVerbindung;
import org.nill.zahlungen.values.IBAN;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@EntityScan(basePackageClasses= {org.nill.abrechnung.entities.Abrechnung.class,org.nill.abrechnung.entities.ZahlungsAuftrag.class })
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
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
        abrechnung.setIMandant(mandant);
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4, 2018));

        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung = abrechnungRepository.save(abrechnung);
        mandant.addAbrechnung(abrechnung);
        return abrechnung;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(umgebung());
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
        IAbrechnung abrechnung = saldoAbgleichen(-100);
        IAbrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06,
                0.19, 360);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schuldenÜbenehmen180() {
        IAbrechnung abrechnung = saldoAbgleichen(-100);
        IAbrechnung nächsteAbrechnung = schuldenÜbernahme(abrechnung, 0.06,
                0.19, 180);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        fülleParameter();
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, -100);
        IAbrechnung nächsteAbrechnung = abschließen(abrechnung);
        checkAnzahlen();
        checkÜbernahme(nächsteAbrechnung, -100, -3);
    }

    private IAbrechnung abschließen(IAbrechnung abrechnung) {
        IAbrechnung nächsteAbrechnung = abrechnung.abschließen(
                umgebung());
        return nächsteAbrechnung;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        fülleParameter();
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);

        IAbrechnung nächsteAbrechnung = abschließen(abrechnung);
        checkÜbernahmeOhneWirkung(nächsteAbrechnung);
    }

    public IAbrechnung saldoAbgleichen(double betrag) {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, betrag);

        SaldoAusgleichen abschluss = new SaldoAusgleichen(umgebung(),
                "Guthaben", "Schulden");
        abschluss.saldoAusgleichen(abrechnung);
        return abrechnung;
    }

    private void erzeugeBuchung(Abrechnung abrechnung, double betrag) {
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag(betrag);
        EinBucher bucher = erzeugeEinbucher();
        IBuchung buchung = bucher.erzeugeBuchung(auftrag, abrechnung);
    }

    public void checkAbgleich(IAbrechnung abrechnung, int art,
            SachKonto kontonr, double betrag) {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        for (IBuchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }

        checkKontoBetrag(abrechnung, art, kontonr, betrag);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo.isZero());

    }

    private void checkKontoBetrag(IAbrechnung abrechnung, int art,
            SachKonto kontonr, double betrag) {
        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                art, kontonr.ordinal());
        if (dbBetrag == null) {
            dbBetrag = Geld.getNull();
        }
        assertEquals(Geld.createAmount(betrag), dbBetrag);
    }

    public IAbrechnung schuldenÜbernahme(IAbrechnung abrechnung, double zinssatz,
            double mwstsatz, int tage) {
        Umgebung provider = umgebung();
        IAbrechnung nächsteAbrechnung = abrechnung
                .createOrGetNächsteAbrechnung(provider);

        SchuldenInDieAbrechnung übernehmen = new SchuldenInDieAbrechnung(
                umgebung(), "Schulden übernehmen", zinssatz, mwstsatz);
        übernehmen.übertragen(nächsteAbrechnung, tage);
        return nächsteAbrechnung;
    }

    public void checkÜbernahme(IAbrechnung abrechnung, double betrag, double zins) {

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

    public void checkÜbernahmeOhneWirkung(IAbrechnung abrechnung) {
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
        fülleParameter();
        Mandant mandant = erzeugeMandant();
        mandant = addZahlungsDefinition(mandant, 0.3);
        mandant = addZahlungsDefinition(mandant, 0.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);
        IAbrechnung nächsteAbrechnung = abschließen(abrechnung);

        ZahlungsAufträgeErzeugen zahlungenManager = new ZahlungsAufträgeErzeugen(
                umgebung());

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
        mandant.addZahlungsDefinitionen(d);
        return mandantRepository.save(mandant);
    }
    
    private void fülleParameter() {
        fülleParameter("180");
    }

}
