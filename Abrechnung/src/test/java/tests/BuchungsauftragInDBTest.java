package tests;

import static org.junit.Assert.assertEquals;

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
import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.KontoBewegung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class BuchungsauftragInDBTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Autowired
    private KontoBewegungRepository kontoBewegungRepository;

    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag() {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        beträge.put(TestSachKonto.BETRAG, Geld.createAmount(1.12));
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

    public Buchung erzeugeBuchung(BuchungsAuftrag<SachKonto> auftrag,
            Abrechnung abrechnung) {
        BetragsBündel<SachKonto> beträge = auftrag.getPositionen();
        Buchung buchung = new Buchung();
        buchung.setText(auftrag.getBeschreibung().getText());
        buchung.setArt(auftrag.getBeschreibung().getArt());
        buchung.setAbrechnung(abrechnung);
        buchung = buchungRepository.save(buchung);
        for (SachKonto p : beträge.getKeys()) {
            KontoBewegung bew = new KontoBewegung();
            bew.setBetrag(beträge.getValue(p));
            bew.setArt(1);
            bew.setKontoNr(p.ordinal());
            bew = kontoBewegungRepository.save(bew);
            bew.setBuchung(buchung);
            buchung.addBewegungen(bew);
        }
        return buchung;
    }

    public BetragsBündel<SachKonto> beträgeEinerBuchungsartHolen(
            Abrechnung abrechnung, int art) {
        BetragsBündelMap<SachKonto> beträge = new BetragsBündelMap<>();
        for (Object o : buchungRepository.getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = TestSachKonto.values()[(int) werte[0]];
            beträge.put(p, (MonetaryAmount) werte[1]);
        }

        return beträge;
    }

    @Test
    public void insertBuchungTest() {
        insertBuchung();
        check();
    }

    @Transactional("dbATransactionManager")
    public Buchung insertBuchung() {
        Mandant mandant = erzeugeMandant();

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag();
        Buchung buchung = erzeugeBuchung(auftrag, abrechnung);

        return buchungRepository.save(buchung);
    }

    @Transactional("dbATransactionManager")
    public void check() {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(1, buchungRepository.count());

        for (Buchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }
    }

    @Test
    public void insertAndLoadTest() {
        Buchung buchung = insertBuchung();
        BetragsBündel<SachKonto> beträge = beträgeEinerBuchungsartHolen(
                buchung.getAbrechnung(), BuchungsArt.TESTBUCHUNG);
        assertEquals(Geld.createAmount(1.12),
                beträge.getValue(TestSachKonto.BETRAG));
    }

    @Test
    public void saldoTest() {
        Buchung buchung = insertBuchung();
        assertEquals(Geld.createAmount(1.12),
                buchungRepository.getSaldo(buchung.getAbrechnung()));
    }

}
