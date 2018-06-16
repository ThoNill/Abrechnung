package tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import app.entities.Abrechnung;
import app.entities.Buchung;
import app.entities.Mandant;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;
import app.repositories.MandantRepository;
import betrag.Geld;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.db.TestDbConfig.class })
public class EinBucherTest {

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

    public BuchungsAuftrag<Position> erzeugeBuchungsAuftrag() {
        BetragsBündelMap<Position> beträge = new BetragsBündelMap<>();
        beträge.put(Position.BETRAG, Geld.createAmount(1.12));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<Position>(beschreibung, beträge);
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

    @Test
    public void insertBuchungTest() {
        insertBuchung();
        check();
    }

    @Transactional("dbATransactionManager")
    public Buchung insertBuchung() {
        Mandant mandant = erzeugeMandant();

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag();
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeBuchung(auftrag, abrechnung);

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

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(buchungRepository, kontoBewegungRepository);
    }

    @Test
    public void insertAndLoadTest() {
        Buchung buchung = insertBuchung();
        EinBucher bucher = erzeugeEinbucher();
        BetragsBündel<Position> beträge = bucher.beträgeEinerBuchungsartHolen(
                buchung.getAbrechnung(), BuchungsArt.TESTBUCHUNG);
        assertEquals(Geld.createAmount(1.12), beträge.getValue(Position.BETRAG));
    }

    @Test
    public void saldoTest() {
        Buchung buchung = insertBuchung();
        assertEquals(Geld.createAmount(1.12),
                buchungRepository.getSaldo(buchung.getAbrechnung()));
    }

    @Transactional("dbATransactionManager")
    public void erzeugeDifferenBuchung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag();
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);

    }

    @Test
    public void differenBuchungTest() {
        erzeugeDifferenBuchung();
        check();
    }

    @Transactional("dbATransactionManager")
    public void erzeugeDoppelteDifferenBuchung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag();
        EinBucher bucher = erzeugeEinbucher();
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);
    }

    @Test
    public void differenDoppelteBuchungTest() {
        erzeugeDoppelteDifferenBuchung();
        check();
    }

}
