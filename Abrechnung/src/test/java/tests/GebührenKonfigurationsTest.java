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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tests.flow.TestAbrechnungsKonfigurator;
import tests.flow.TestLeistungsRepository;
import tests.konten.TestSachKonto;
import tests.konten.TestSachKontoProvider;
import betrag.Geld;
import boundingContext.abrechnung.aufz�hlungen.BuchungsArt;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.entities.Leistung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.handler.AbrechnungsKonfigurator;
import boundingContext.abrechnung.helper.Geb�hrenBerechnung;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.Geb�hrenDefinitionRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsB�ndel;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class Geb�hrenKonfigurationsTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private Geb�hrenDefinitionRepository geb�hrenDefinitinRepository;

    @Autowired
    private LeistungRepository leistungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Autowired
    private KontoBewegungRepository kontoBewegungRepository;

    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        leistungRepository.deleteAll();
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        abrechnungRepository.deleteAll();
        geb�hrenDefinitinRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void mitEinemMandantenVerbinden() {
        GebuehrDefinition d = erzeugeGeb�hrDefinition();

        verbindeMitEinemMandaten(d);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, geb�hrenDefinitinRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void mitMehrerenMandantenVerbinden() {
        GebuehrDefinition d = erzeugeGeb�hrDefinition();

        verbindeMitEinemMandaten(d);
        verbindeMitEinemMandaten(d);
        verbindeMitEinemMandaten(d);
        assertEquals(3, mandantRepository.count());
        assertEquals(1, geb�hrenDefinitinRepository.count());
    }

    private void verbindeMitEinemMandaten(GebuehrDefinition d) {
        Mandant mandant = erzeugeMandant();
        verbindeMitMandanten(mandant, d);
    }

    private Mandant verbindeMitMandanten(Mandant mandant, GebuehrDefinition d) {
        d.addMandant(mandant);
        d = geb�hrenDefinitinRepository.save(d);
        mandant.addGebuehrDefinitionen(d);
        return mandantRepository.save(mandant);
    }

    private GebuehrDefinition erzeugeGeb�hrDefinition() {
        GebuehrDefinition d = new GebuehrDefinition();
        d.setArt(BuchungsArt.TESTBUCHUNG);
        d.setKontoNr(TestSachKonto.GEB�HR.ordinal());
        d.setGeb�hrArt(1);
        d.setDatenArt(1);
        d.setParameter(0.05);
        d = geb�hrenDefinitinRepository.save(d);
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

        GebuehrDefinition geb�hrDefinition = new GebuehrDefinition();
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
        Geb�hrenBerechnung berechnung = konfigurator
                .erzeugeGeb�hrenBerechner(geb�hrDefinition);
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(abrechnung);
        BetragsB�ndel<SachKonto> auftragB�ndel = auftrag.getPositionen();

        assertEquals(Geld.createAmount(summe),
                auftragB�ndel.getBetrag(TestSachKonto.BETRAG));
        assertEquals(Geld.createAmount(-summe * 0.06),
                auftragB�ndel.getBetrag(TestSachKonto.GEB�HR));
        assertEquals(Geld.createAmount(-summe * 0.06 * 0.19),
                auftragB�ndel.getBetrag(TestSachKonto.MWST));

        EinBucher bucher = new EinBucher(new TestSachKontoProvider(),
                buchungRepository, kontoBewegungRepository);
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);
        // Noch einmal, darf nichts ausmachen
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);

        BetragsB�ndel<SachKonto> b�ndel = bucher.betr�geEinerBuchungsartHolen(
                abrechnung, BuchungsArt.TESTBUCHUNG);

        assertEquals(Geld.createAmount(summe),
                b�ndel.getBetrag(TestSachKonto.BETRAG));
        assertEquals(Geld.createAmount(-summe * 0.06),
                b�ndel.getBetrag(TestSachKonto.GEB�HR));
        assertEquals(Geld.createAmount(-summe * 0.06 * 0.19),
                b�ndel.getBetrag(TestSachKonto.MWST));

        assertEquals(3, leistungRepository.count());

    }

}
