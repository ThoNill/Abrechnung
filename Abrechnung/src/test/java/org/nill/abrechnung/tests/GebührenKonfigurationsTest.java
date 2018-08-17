package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.BuchungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Geb�hrDefinition;
import org.nill.abrechnung.entities.Leistung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IGeb�hrBerechnung;
import org.nill.abrechnung.interfaces.IGeb�hrDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.repositories.LeistungRepository;
import org.nill.abrechnung.tests.flow.TestAbrechnungsKonfigurator;
import org.nill.abrechnung.tests.flow.TestLeistungsRepository;
import org.nill.abrechnung.tests.konten.TestSachKonto;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Geb�hrenKonfigurationsTest extends AbrechnungBasisTest {

    @Autowired
    private LeistungRepository leistungRepository;

    @Override
    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        super.clear();
        leistungRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void mitEinemMandantenVerbinden() {
        Geb�hrDefinition d = erzeugeGeb�hrDefinition();

        verbindeMitEinemMandaten(d);
        assertEquals(1, mandantRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void mitMehrerenMandantenVerbinden() {
        Geb�hrDefinition d = erzeugeGeb�hrDefinition();

        verbindeMitEinemMandaten(d);
        verbindeMitEinemMandaten(d);
        verbindeMitEinemMandaten(d);
        assertEquals(3, mandantRepository.count());
    }

    private void verbindeMitEinemMandaten(Geb�hrDefinition d) {
        Mandant mandant = erzeugeMandant();
        verbindeMitMandanten(mandant, d);
    }

    private IMandant verbindeMitMandanten(Mandant mandant, Geb�hrDefinition d) {
        d.addMandant(mandant);
        mandant.addGebuehrDefinitionen(d);
        return mandantRepository.save(mandant);
    }

    private Geb�hrDefinition erzeugeGeb�hrDefinition() {
        Geb�hrDefinition d = new Geb�hrDefinition();
        d.setArt(BuchungsArt.TESTBUCHUNG);
        d.setKontoNr(TestSachKonto.GEB�HR.ordinal());
        d.setGeb�hrArt(1);
        d.setDatenArt(1);
        d.setParameter(0.05);
        return d;
    }

    public Leistung erzeugeEineLeistung(Mandant mandant, int art, double betrag) {
        Leistung l = new Leistung();
        l.setMandant(mandant);
        l.setArt(art);
        l.setBetrag(Geld.createAmount(betrag));
        return leistungRepository.save(l);
    }

    public double erzeugeLeistungen(Mandant mandant, int art, double... betr�ge) {
        double summe = 0.0;
        for (double betrag : betr�ge) {
            erzeugeEineLeistung(mandant, art, betrag);
            summe += betrag;
        }
        return summe;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void erzeugeLeistungenTesten() {
        erzeugeLeistungen(erzeugeMandant(), 1, 2.3, 2.5, 6.7);
        assertEquals(3, leistungRepository.count());
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

    @Test
    @Transactional("dbATransactionManager")
    public void dasTestLeistungsRepositoryTesten() {
        Mandant mandant = erzeugeMandant();
        double summe = erzeugeLeistungen(mandant, 1, 2.3, 2.5, 6.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        TestLeistungsRepository leistungsRepository = new TestLeistungsRepository();
        leistungsRepository.setLeistungsRepository(leistungRepository);
        MonetaryAmount betrag = leistungsRepository
                .getGeb�hrenBasis(abrechnung);
        assertTrue(betrag == null || betrag.isZero());
        leistungsRepository.markieren(abrechnung);
        betrag = leistungsRepository.getGeb�hrenBasis(abrechnung);
        assertEquals(Geld.createAmount(summe), betrag);
        assertEquals(3, leistungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void geb�hrenBerechnen() {
        Mandant mandant = erzeugeMandant();
        double summe = erzeugeLeistungen(mandant, 1, 2.3, 2.5, 6.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        TestLeistungsRepository leistungsRepository = new TestLeistungsRepository();
        leistungsRepository.setLeistungsRepository(leistungRepository);

        IGeb�hrDefinition geb�hrDefinition = new Geb�hrDefinition();
        geb�hrDefinition.setArt(BuchungsArt.TESTBUCHUNG);
        geb�hrDefinition.setKontoNr(TestSachKonto.GEB�HR.ordinal());
        geb�hrDefinition.setGeb�hrArt(1);
        geb�hrDefinition.setDatenArt(1);
        geb�hrDefinition.setParameter(0.06);
        geb�hrDefinition.setMwstKonto(TestSachKonto.MWST.ordinal());
        geb�hrDefinition.setMwstSatz(0.19);
        geb�hrDefinition.setBuchungsArt(BuchungsArt.TESTBUCHUNG);
        geb�hrDefinition.setBuchungstext("Testbuchung");

        AbrechnungsKonfigurator konfigurator = new TestAbrechnungsKonfigurator(
                leistungRepository);
        IGeb�hrBerechnung berechnung = konfigurator.erzeugeGeb�hrenBerechner(
                geb�hrDefinition, sachKontoProvider(), AbrechnungsArt.NEU);
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(abrechnung);
        BetragsB�ndel<SachKonto> auftragB�ndel = auftrag.getPositionen();

        assertEquals(Geld.createAmount(summe),
                auftragB�ndel.getBetrag(TestSachKonto.BETRAG));
        assertEquals(Geld.createAmount(-summe * 0.06),
                auftragB�ndel.getBetrag(TestSachKonto.GEB�HR));
        assertEquals(Geld.createAmount(-summe * 0.06 * 0.19),
                auftragB�ndel.getBetrag(TestSachKonto.MWST));

        EinBucher bucher = new EinBucher(sachKontoProvider());
        // Noch einmal, darf nichts ausmachen

        abrechnung = abrechnungRepository.save(abrechnung);
        
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);

        BetragsB�ndel<SachKonto> b�ndel = bucher.betr�geEinerBuchungsartHolen(
                abrechnung, BuchungsArt.TESTBUCHUNG);

        assertEquals(Geld.createAmount(summe),
                b�ndel.getBetrag(TestSachKonto.BETRAG));
        assertEquals(Geld.createAmount(-summe * 0.06),
                b�ndel.getBetrag(TestSachKonto.GEB�HR));
        assertEquals(Geld.createAmount(-summe * 0.06 * 0.19),
                b�ndel.getBetrag(TestSachKonto.MWST));

        assertEquals(1, abrechnungRepository.count());
        assertEquals(3, leistungRepository.count());

    }

}
