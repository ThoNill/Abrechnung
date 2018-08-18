package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.flow.handler.HoleAbrechnung;
import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.flow.payloads.AufrufPayload;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.allgemein.values.MonatJahr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class HoleAbrechnungsTest extends AbrechnungBasisTest {
    protected static final Logger LOG = LoggerFactory
            .getLogger(HoleAbrechnungsTest.class);
  

    @Override
    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return new Mandant();
    }

    public Abrechnung erzeugeAbrechnung(Mandant mandant, int nummer) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setIMandant(mandant);
        abrechnung.setNummer(nummer);
        abrechnung.setMj(new MonatJahr(4, 2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        mandant.addAbrechnung(abrechnung);
        return abrechnungRepository.save(abrechnung);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void holeEineBestimmteAbrechnung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant, 1);
        AufrufPayload parameter = new AufrufPayload(
                AbrechnungsArt.NACHBERCHNEN, mandant.getMandantId(),
                abrechnung.getAbrechnungId(), new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        aufruf(parameter);

        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnungNachberechnenOderErgänzen() {
        IMandant mandant = mandantRepository.save(erzeugeMandant());
        AufrufPayload parameter = new AufrufPayload(
                AbrechnungsArt.NACHBERCHNEN, mandant.getMandantId(), 0,
                new MonatJahr(4, 2018), AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

        parameter = new AufrufPayload(AbrechnungsArt.ERGÄNZEN,
                mandant.getMandantId(), 0, new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

        assertEquals(1, mandantRepository.count());
        assertEquals(0, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnungNachberechnenOderErgänzen2() {
        IMandant mandant = mandantRepository.save(erzeugeMandant());
        AufrufPayload parameter = new AufrufPayload(
                AbrechnungsArt.NACHBERCHNEN, mandant.getMandantId(), 3,
                new MonatJahr(4, 2018), AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

        parameter = new AufrufPayload(AbrechnungsArt.ERGÄNZEN,
                mandant.getMandantId(), 3, new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

        assertEquals(1, mandantRepository.count());
        assertEquals(0, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnungNachberechnenOderErgänzen3() {
        Mandant mandant = mandantRepository.save(erzeugeMandant());
        Abrechnung abrechnung = erzeugeAbrechnung(mandant, 1);
        AufrufPayload parameter = new AufrufPayload(
                AbrechnungsArt.NACHBERCHNEN, mandant.getMandantId(),
                abrechnung.getAbrechnungId(), new MonatJahr(5, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

        parameter = new AufrufPayload(AbrechnungsArt.ERGÄNZEN,
                mandant.getMandantId(), abrechnung.getAbrechnungId(),
                new MonatJahr(5, 2018), AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void ohneAbrechnung() {
        IMandant mandant = mandantRepository.save(erzeugeMandant());
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), 0, new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        AbrechnungPayload testErgebnis = aufruf(parameter);

        assertEquals(1, testErgebnis.getAbrechnung().getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void rechneLetzteAbrechnungAb() {
        Mandant mandant = erzeugeMandant();
        erzeugeAbrechnung(mandant, 1);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), 0, new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        AbrechnungPayload testErgebnis = aufruf(parameter);

        assertEquals(1, testErgebnis.getAbrechnung().getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void rechneNeueAbrechnungAb() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant, 1);
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), 0, new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        AbrechnungPayload testErgebnis = aufruf(parameter);

        assertEquals(2, testErgebnis.getAbrechnung().getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void rechneNeueAbrechnungAbAusnahme() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant, 1);
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        AufrufPayload parameter = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), abrechnung.getAbrechnungId(),
                new MonatJahr(4, 2018), AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);

    }

    @Test
    @Transactional("dbATransactionManager")
    public void falscherMandant() {
        IMandant mandant1 = mandantRepository.save(erzeugeMandant());
        Mandant mandant2 = mandantRepository.save(erzeugeMandant());
        Abrechnung abrechnung = erzeugeAbrechnung(mandant2, 1);
        AufrufPayload parameter = new AufrufPayload(
                AbrechnungsArt.NACHBERCHNEN, mandant1.getMandantId(),
                abrechnung.getAbrechnungId(), new MonatJahr(4, 2018),
                AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void keinMandant() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant, 1);
        AufrufPayload parameter = new AufrufPayload(
                AbrechnungsArt.NACHBERCHNEN, 0, abrechnung.getAbrechnungId(),
                new MonatJahr(4, 2018), AbrechnungsTyp.MONATSABRECHNUNG);

        aufrufMitException(parameter);
    }

    private AbrechnungPayload aufruf(AufrufPayload parameter) {
        HoleAbrechnung handler = new HoleAbrechnung(umgebung());
        try {
            return handler.testTransformPayload(parameter);

        } catch (Exception e) {
            fail("Unerwartete Ausnahme");
            LOG.error(e.getMessage());
        }
        return null;
    }

    private void aufrufMitException(AufrufPayload parameter) {
        HoleAbrechnung handler = new HoleAbrechnung(umgebung());

        try {
            handler.testTransformPayload(parameter);

            fail("Ausnahme erwartet");
        } catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }

}
