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
import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.BuchungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebührDefinition;
import org.nill.abrechnung.entities.Leistung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IGebührBerechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.repositories.LeistungRepository;
import org.nill.abrechnung.tests.flow.TestAbrechnungsKonfigurator;
import org.nill.abrechnung.tests.flow.TestLeistungsRepository;
import org.nill.abrechnung.tests.konten.TestSachKonto;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class GebührenKonfigurationsTest extends AbrechnungBasisTest {

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
        GebührDefinition d = erzeugeGebührDefinition();

        verbindeMitEinemMandaten(d);
        assertEquals(1, mandantRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void mitMehrerenMandantenVerbinden() {
        GebührDefinition d = erzeugeGebührDefinition();

        verbindeMitEinemMandaten(d);
        verbindeMitEinemMandaten(d);
        verbindeMitEinemMandaten(d);
        assertEquals(3, mandantRepository.count());
    }

    private void verbindeMitEinemMandaten(GebührDefinition d) {
        Mandant mandant = erzeugeMandant();
        verbindeMitMandanten(mandant, d);
    }

    private IMandant verbindeMitMandanten(Mandant mandant, GebührDefinition d) {
        d.addMandant(mandant);
        mandant.addGebuehrDefinitionen(d);
        return mandantRepository.save(mandant);
    }

    private GebührDefinition erzeugeGebührDefinition() {
        GebührDefinition d = new GebührDefinition();
        d.setArt(BuchungsArt.TESTBUCHUNG);
        d.setKontoNr(TestSachKonto.GEBÜHR.ordinal());
        d.setGebührArt(1);
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

    public double erzeugeLeistungen(Mandant mandant, int art, double... beträge) {
        double summe = 0.0;
        for (double betrag : beträge) {
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
                .getGebührenBasis(abrechnung);
        assertTrue(betrag == null || betrag.isZero());
        leistungsRepository.markieren(abrechnung);
        betrag = leistungsRepository.getGebührenBasis(abrechnung);
        assertEquals(Geld.createAmount(summe), betrag);
        assertEquals(3, leistungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void gebührenBerechnen() {
        Mandant mandant = erzeugeMandant();
        double summe = erzeugeLeistungen(mandant, 1, 2.3, 2.5, 6.7);

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        TestLeistungsRepository leistungsRepository = new TestLeistungsRepository();
        leistungsRepository.setLeistungsRepository(leistungRepository);

        IGebührDefinition gebührDefinition = new GebührDefinition();
        gebührDefinition.setArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setKontoNr(TestSachKonto.GEBÜHR.ordinal());
        gebührDefinition.setGebührArt(1);
        gebührDefinition.setDatenArt(1);
        gebührDefinition.setParameter(0.06);
        gebührDefinition.setMwstKonto(TestSachKonto.MWST.ordinal());
        gebührDefinition.setMwstSatz(0.19);
        gebührDefinition.setBuchungsArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setBuchungstext("Testbuchung");

        AbrechnungsKonfigurator konfigurator = new TestAbrechnungsKonfigurator(
                leistungRepository);
        IGebührBerechnung berechnung = konfigurator.erzeugeGebührenBerechner(
                gebührDefinition, sachKontoProvider(), AbrechnungsArt.NEU);
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(abrechnung);
        BetragsBündel<SachKonto> auftragBündel = auftrag.getPositionen();

        assertEquals(Geld.createAmount(summe),
                auftragBündel.getBetrag(TestSachKonto.BETRAG));
        assertEquals(Geld.createAmount(-summe * 0.06),
                auftragBündel.getBetrag(TestSachKonto.GEBÜHR));
        assertEquals(Geld.createAmount(-summe * 0.06 * 0.19),
                auftragBündel.getBetrag(TestSachKonto.MWST));

        EinBucher bucher = new EinBucher(sachKontoProvider());
        // Noch einmal, darf nichts ausmachen

        abrechnung = abrechnungRepository.save(abrechnung);
        
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);

        BetragsBündel<SachKonto> bündel = bucher.beträgeEinerBuchungsartHolen(
                abrechnung, BuchungsArt.TESTBUCHUNG);

        assertEquals(Geld.createAmount(summe),
                bündel.getBetrag(TestSachKonto.BETRAG));
        assertEquals(Geld.createAmount(-summe * 0.06),
                bündel.getBetrag(TestSachKonto.GEBÜHR));
        assertEquals(Geld.createAmount(-summe * 0.06 * 0.19),
                bündel.getBetrag(TestSachKonto.MWST));

        assertEquals(1, abrechnungRepository.count());
        assertEquals(3, leistungRepository.count());

    }

}
