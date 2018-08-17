package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import javax.money.MonetaryAmount;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufz�hlungen.BuchungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.tests.konten.TestSachKonto;
import org.nill.abrechnung.values.KontoBewegung;
import org.nill.allgemein.values.MonatJahr;
import org.nill.basiskomponenten.betrag.Geld;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;
import org.nill.buchhaltung.eingang.Beschreibung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class BuchungsauftragInDBTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        buchungRepository.deleteAll();
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public BuchungsAuftrag<SachKonto> erzeugeBuchungsAuftrag() {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        betr�ge.put(TestSachKonto.BETRAG, Geld.createAmount(1.12));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<SachKonto>(beschreibung, betr�ge);
    }

    public Mandant erzeugeMandant() {
        return new Mandant();
    }

    public IAbrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setIMandant(mandant);
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4, 2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        mandant.addAbrechnung(abrechnung);
        return abrechnung;
    }

    public Buchung erzeugeBuchung(BuchungsAuftrag<SachKonto> auftrag,
            IAbrechnung abrechnung) {
        BetragsB�ndel<SachKonto> betr�ge = auftrag.getPositionen();
        Buchung buchung = new Buchung();
        buchung.setText(auftrag.getBeschreibung().getText());
        buchung.setArt(auftrag.getBeschreibung().getArt());
        buchung.setIAbrechnung(abrechnung);
        for (SachKonto p : betr�ge.getKeys()) {
            KontoBewegung bew = new KontoBewegung();
            bew.setBetrag(betr�ge.getValue(p));
            bew.setArt(1);
            bew.setKontoNr(p.ordinal());
            buchung.addBewegungen(bew);
        }
        return buchung;
    }

    public BetragsB�ndel<SachKonto> betr�geEinerBuchungsartHolen(
            IAbrechnung abrechnung, int art) {
        BetragsB�ndelMap<SachKonto> betr�ge = new BetragsB�ndelMap<>();
        for (Object o : buchungRepository.getSumBewegungen(abrechnung, art)) {
            Object[] werte = (Object[]) o;
            SachKonto p = TestSachKonto.values()[(int) werte[0]];
            betr�ge.put(p, (MonetaryAmount) werte[1]);
        }

        return betr�ge;
    }

    @Test
    @Transactional("dbATransactionManager")
    public void insertBuchungTest() {
        insertBuchung();
        check();
    }

    public IBuchung insertBuchung() {
        Mandant mandant = erzeugeMandant();

        IAbrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<SachKonto> auftrag = erzeugeBuchungsAuftrag();
        Buchung buchung = erzeugeBuchung(auftrag, abrechnung);
        return buchungRepository.save(buchung);
    }

    @Transactional("dbATransactionManager")
    public void check() {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(1, buchungRepository.count());

        for (IBuchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }

    }

    @Test
    public void insertAndLoadTest() {
        IBuchung buchung = insertBuchung();
        BetragsB�ndel<SachKonto> betr�ge = betr�geEinerBuchungsartHolen(
                buchung.getAbrechnung(), BuchungsArt.TESTBUCHUNG);
        assertEquals(Geld.createAmount(1.12),
                betr�ge.getValue(TestSachKonto.BETRAG));
    }

    @Test
    public void saldoTest() {
        IBuchung buchung = insertBuchung();
        assertEquals(Geld.createAmount(1.12),
                buchungRepository.getSaldo(buchung.getAbrechnung()));
    }

}
