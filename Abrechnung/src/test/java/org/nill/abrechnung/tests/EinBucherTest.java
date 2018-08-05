package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufz�hlungen.BuchungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.tests.konten.TestSachKonto;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.nill.buchhaltung.eingang.EinBucher;
import org.nill.zahlungen.values.MonatJahr;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class EinBucherTest extends AbrechnungBasisTest {

    public BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag() {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(TestSachKonto.BETRAG, Geld.createAmount(1.12));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<SachKonto>(beschreibung, betr�ge);
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
        BetragsB�ndel<SachKonto> betr�ge = bucher.betr�geEinerBuchungsartHolen(
                buchung.getAbrechnung(), BuchungsArt.TESTBUCHUNG);
        assertEquals(Geld.createAmount(1.12),
                betr�ge.getValue(TestSachKonto.BETRAG));
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
