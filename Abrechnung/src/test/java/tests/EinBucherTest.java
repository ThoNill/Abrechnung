package tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import tests.konten.TestSachKonto;
import betrag.Geld;
import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Buchung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;
import boundingContext.zahlungen.values.MonatJahr;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class EinBucherTest extends AbrechnungBasisTest {

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
        abrechnung.setMj(new MonatJahr(4,2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        abrechnung = abrechnungRepository.save(abrechnung);
        mandant.addAbrechnung(abrechnung);
        return abrechnung;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void insertBuchungTest() {
        insertBuchung();
        check();
    }

    public Buchung insertBuchung() {
        Mandant mandant = erzeugeMandant();

        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag();
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeBuchung(auftrag, abrechnung);

        return buchungRepository.save(buchung);
    }

    public void check() {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(1, buchungRepository.count());

        for (Buchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }
    }

  
    
    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void insertAndLoadTest() {
        Buchung buchung = insertBuchung();
        EinBucher bucher = erzeugeEinbucher();
        BetragsBündel<SachKonto> beträge = bucher.beträgeEinerBuchungsartHolen(
                buchung.getAbrechnung(), BuchungsArt.TESTBUCHUNG);
        assertEquals(Geld.createAmount(1.12),
                beträge.getValue(TestSachKonto.BETRAG));
    }

    @Test
    @Transactional("dbATransactionManager")
    public void saldoTest() {
        Buchung buchung = insertBuchung();
        assertEquals(Geld.createAmount(1.12),
                buchungRepository.getSaldo(buchung.getAbrechnung()));
    }

    public void erzeugeDifferenBuchung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag();
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);

    }

    @Test
    @Transactional("dbATransactionManager")
    public void differenBuchungTest() {
        erzeugeDifferenBuchung();
        check();
    }

    public void erzeugeDoppelteDifferenBuchung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag();
        EinBucher bucher = erzeugeEinbucher();
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);
        bucher.erzeugeDifferenzBuchung(auftrag, abrechnung);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void differenDoppelteBuchungTest() {
        erzeugeDoppelteDifferenBuchung();
        check();
    }

}
