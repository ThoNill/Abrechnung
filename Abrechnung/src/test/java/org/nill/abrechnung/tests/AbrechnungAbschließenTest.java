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
import org.nill.abrechnung.aufz�hlungen.BuchungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.tests.konten.TestSachKonto;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.zahlungen.actions.ZahlungsAuftr�geErzeugen;
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
public class AbrechnungAbschlie�enTest extends AbrechnungBasisTest {

    public BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag(double betrag) {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(TestSachKonto.BETRAG, Geld.createAmount(betrag));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<SachKonto>(beschreibung, betr�ge);
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
    public void schulden�benehmen360() {
        IAbrechnung abrechnung = saldoAbgleichen(-100);
        IAbrechnung n�chsteAbrechnung = schulden�bernahme(abrechnung, 0.06,
                0.19, 360);
        checkAnzahlen();
        check�bernahme(n�chsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schulden�benehmen180() {
        IAbrechnung abrechnung = saldoAbgleichen(-100);
        IAbrechnung n�chsteAbrechnung = schulden�bernahme(abrechnung, 0.06,
                0.19, 180);
        checkAnzahlen();
        check�bernahme(n�chsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        f�lleParameter();
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, -100);
        IAbrechnung n�chsteAbrechnung = abschlie�en(abrechnung);
        checkAnzahlen();
        check�bernahme(n�chsteAbrechnung, -100, -3);
    }

    private IAbrechnung abschlie�en(IAbrechnung abrechnung) {
        IAbrechnung n�chsteAbrechnung = abrechnung.abschlie�en(
                umgebung());
        return n�chsteAbrechnung;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        f�lleParameter();
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);

        IAbrechnung n�chsteAbrechnung = abschlie�en(abrechnung);
        check�bernahmeOhneWirkung(n�chsteAbrechnung);
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

    public IAbrechnung schulden�bernahme(IAbrechnung abrechnung, double zinssatz,
            double mwstsatz, int tage) {
        Umgebung provider = umgebung();
        IAbrechnung n�chsteAbrechnung = abrechnung
                .createOrGetN�chsteAbrechnung(provider);

        SchuldenInDieAbrechnung �bernehmen = new SchuldenInDieAbrechnung(
                umgebung(), "Schulden �bernehmen", zinssatz, mwstsatz);
        �bernehmen.�bertragen(n�chsteAbrechnung, tage);
        return n�chsteAbrechnung;
    }

    public void check�bernahme(IAbrechnung abrechnung, double betrag, double zins) {

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.�BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.�BERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        MonetaryAmount dbMwst = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.�BERNAHME_SCHULDEN, TestSachKonto.MWST.ordinal());
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

    public void check�bernahmeOhneWirkung(IAbrechnung abrechnung) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.�BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.�BERNAHME_SCHULDEN, TestSachKonto.ZINS.ordinal());
        assertTrue(dbBetrag == null || dbBetrag.isZero());
        assertTrue(dbZins == null || dbZins.isZero());
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo == null || saldo.isZero());

    }

    @Test
    @Transactional("dbATransactionManager")
    public void mehrmaligerAbschluss() {
        f�lleParameter();
        Mandant mandant = erzeugeMandant();
        mandant = addZahlungsDefinition(mandant, 0.3);
        mandant = addZahlungsDefinition(mandant, 0.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        erzeugeBuchung(abrechnung, 100);
        IAbrechnung n�chsteAbrechnung = abschlie�en(abrechnung);

        ZahlungsAuftr�geErzeugen zahlungenManager = new ZahlungsAuftr�geErzeugen(
                umgebung());

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -100);
        checkKontoBetrag(abrechnung, BuchungsArt.�BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, -40);
        n�chsteAbrechnung = abschlie�en(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -60);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, -60);
        n�chsteAbrechnung = abschlie�en(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        erzeugeBuchung(abrechnung, 20);
        n�chsteAbrechnung = abschlie�en(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);

        zahlungenManager.erzeugeAuftr�ge(abrechnung, Geld.createAmount(20),
                "Test");

        erzeugeBuchung(abrechnung, -40);
        n�chsteAbrechnung = abschlie�en(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 20);
        checkKontoBetrag(n�chsteAbrechnung, BuchungsArt.�BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, -20);

        erzeugeBuchung(abrechnung, -40);
        n�chsteAbrechnung = abschlie�en(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, 0);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 60);
        checkKontoBetrag(n�chsteAbrechnung, BuchungsArt.�BERNAHME_SCHULDEN,
                TestSachKonto.SCHULDEN, -60);

        erzeugeBuchung(abrechnung, 80);
        n�chsteAbrechnung = abschlie�en(abrechnung);

        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_GUTHABEN,
                TestSachKonto.GUTHABEN, -20);
        checkKontoBetrag(abrechnung, BuchungsArt.ABGLEICH_SCHULDEN,
                TestSachKonto.SCHULDEN, 0);
        checkKontoBetrag(n�chsteAbrechnung, BuchungsArt.�BERNAHME_SCHULDEN,
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
    
    private void f�lleParameter() {
        f�lleParameter("180");
    }

}
