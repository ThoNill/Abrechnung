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
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import app.entities.Abrechnung;
import app.entities.Buchung;
import app.entities.Mandant;
import app.helper.AbrechnungHelper;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;
import app.repositories.MandantRepository;
import betrag.Geld;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.gemeinsam.BetragsB¸ndelMap;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.db.TestDbConfig.class })
public class AbrechnungAbschlieﬂenTest {

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

    public BuchungsAuftrag<Position> erzeugeBuchungsAuftrag(double betrag) {
        BetragsB¸ndelMap<Position> betr‰ge = new BetragsB¸ndelMap<>();
        betr‰ge.put(Position.BETRAG, Geld.createAmount(betrag));
        Beschreibung beschreibung = new Beschreibung(BuchungsArt.TESTBUCHUNG,
                "Testbuchung");

        return new BuchungsAuftrag<Position>(beschreibung, betr‰ge);
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

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(buchungRepository, kontoBewegungRepository);
    }

    @Test
    public void ausgleichGuthaben() {
        checkAbgleich(saldoAbgleichen(1.24), BuchungsArt.ABGLEICH_GUTHABEN,
                Position.GUTHABEN, -1.24);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ausgleichSchulden() {
        checkAbgleich(saldoAbgleichen(-1.24), BuchungsArt.ABGLEICH_SCHULDEN,
                Position.SCHULDEN, 1.24);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schulden‹benehmen360() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung n‰chsteAbrechnung = schulden‹bernahme(abrechnung, 0.06, 360);
        check‹bernahme(n‰chsteAbrechnung, -100, -6);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void schulden‹benehmen180() {
        Abrechnung abrechnung = saldoAbgleichen(-100);
        Abrechnung n‰chsteAbrechnung = schulden‹bernahme(abrechnung, 0.06, 180);
        check‹bernahme(n‰chsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschluss180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag(-100);
        EinBucher bucher = erzeugeEinbucher();
        bucher.erzeugeBuchung(auftrag, abrechnung);

        AbrechnungAbschlieﬂen abchluss = new AbrechnungAbschlieﬂen(
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, 0.06);
        Abrechnung n‰chsteAbrechnung = abchluss.abschleiﬂen(abrechnung, 180);
        check‹bernahme(n‰chsteAbrechnung, -100, -3);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abschlussOhneWirkung180() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag(100);
        EinBucher bucher = erzeugeEinbucher();
        bucher.erzeugeBuchung(auftrag, abrechnung);

        AbrechnungAbschlieﬂen abchluss = new AbrechnungAbschlieﬂen(
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, 0.06);
        Abrechnung n‰chsteAbrechnung = abchluss.abschleiﬂen(abrechnung, 180);
        check‹bernahmeOhneWirkung(n‰chsteAbrechnung);
    }

    @Transactional("dbATransactionManager")
    public Abrechnung saldoAbgleichen(double betrag) {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        BuchungsAuftrag<Position> auftrag = erzeugeBuchungsAuftrag(betrag);
        EinBucher bucher = erzeugeEinbucher();
        Buchung buchung = bucher.erzeugeBuchung(auftrag, abrechnung);

        SaldoAusgleichen abschluss = new SaldoAusgleichen(buchungRepository,
                kontoBewegungRepository, BuchungsArt.ABGLEICH_GUTHABEN,
                Position.GUTHABEN, "Guthaben", BuchungsArt.ABGLEICH_SCHULDEN,
                Position.SCHULDEN, "Schulden");
        abschluss.saldoAusgleichen(abrechnung);
        return abrechnung;
    }

    @Transactional("dbATransactionManager")
    public void checkAbgleich(Abrechnung abrechnung, int art, Position kontonr,
            double betrag) {
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        for (Buchung buchung : buchungRepository.findAll()) {
            assertEquals(1, buchung.getBewegungen().size());
        }

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                art, kontonr.ordinal());
        assertEquals(Geld.createAmount(betrag), dbBetrag);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo.isZero());

    }

    @Transactional("dbATransactionManager")
    public Abrechnung schulden‹bernahme(Abrechnung abrechnung, double zinssatz,
            int tage) {
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Abrechnung n‰chsteAbrechnung = helper
                .createOrGetN‰chsteAbrechnung(abrechnung);

        SchuldenInDieAbrechnung ¸bernehmen = new SchuldenInDieAbrechnung(
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, BuchungsArt.ABGLEICH_SCHULDEN,
                BuchungsArt.‹BERNAHME_SCHULDEN, Position.SCHULDEN,
                Position.ZINS, "Schulden ¸bernehmen", zinssatz);
        ¸bernehmen.¸bertragen(n‰chsteAbrechnung, tage);
        return n‰chsteAbrechnung;
    }

    @Transactional("dbATransactionManager")
    public void check‹bernahme(Abrechnung abrechnung, double betrag, double zins) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(3, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN, Position.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN, Position.ZINS.ordinal());
        assertEquals(Geld.createAmount(betrag), dbBetrag);
        assertEquals(Geld.createAmount(zins), dbZins);
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertEquals(Geld.createAmount(betrag + zins), saldo);

    }

    @Transactional("dbATransactionManager")
    public void check‹bernahmeOhneWirkung(Abrechnung abrechnung) {
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertEquals(2, buchungRepository.count());

        MonetaryAmount dbBetrag = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN, Position.SCHULDEN.ordinal());
        MonetaryAmount dbZins = buchungRepository.getSumKonto(abrechnung,
                BuchungsArt.‹BERNAHME_SCHULDEN, Position.ZINS.ordinal());
        assertTrue(dbBetrag == null || dbBetrag.isZero());
        assertTrue(dbZins == null || dbZins.isZero());
        MonetaryAmount saldo = buchungRepository.getSaldo(abrechnung);
        assertTrue(saldo == null || saldo.isZero());

    }

}
