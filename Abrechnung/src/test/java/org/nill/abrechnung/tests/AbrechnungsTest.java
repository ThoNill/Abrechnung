package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsStatus;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsTyp;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.allgemein.values.MonatJahr;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class AbrechnungsTest extends AbrechnungBasisTest {

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

    public Abrechnung erzeugeAbrechnung(Mandant mandant) {
        Abrechnung abrechnung = new Abrechnung();
        abrechnung.setMandant(mandant);
        abrechnung.setNummer(3);
        abrechnung.setMj(new MonatJahr(4, 2018));
        abrechnung.setBezeichnung("Test");
        abrechnung.setAngelegt(new Date());
        mandant.addAbrechnung(abrechnung);
        return abrechnungRepository.save(abrechnung);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void n�chsteAbrechnungTest() {
        Abrechnung abrechnung = naechsteAbrechnung();
        assertEquals(4, abrechnung.getNummer());
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
    }

    public Abrechnung naechsteAbrechnung() {
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        return abrechnung.createOrGetN�chsteAbrechnung(sachKontoProvider());
    }

    public Abrechnung naechsteAbrechnungMerfacherAufruf() {
        SachKontoProvider provider = sachKontoProvider();
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        abrechnung.createOrGetN�chsteAbrechnung(provider);
        abrechnung.createOrGetN�chsteAbrechnung(provider);
        return abrechnung.createOrGetN�chsteAbrechnung(provider);
    }

    @Test
    @Transactional("dbATransactionManager")
    public void n�chsteAbrechnungMehrfacherAufrufTest() {
        Abrechnung abrechnung = naechsteAbrechnungMerfacherAufruf();
        assertEquals(1, mandantRepository.count());
        assertEquals(4, abrechnung.getNummer());
        assertEquals(2, abrechnungRepository.count());
    }

    public Optional<Abrechnung> vorherigeAbrechnung() {
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        return abrechnung.getVorherigeAbrechnung(sachKontoProvider());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void vorherigeAbrechnungTest() {
        Optional<Abrechnung> oAbrechnung = vorherigeAbrechnung();
        assertFalse(oAbrechnung.isPresent());
        assertEquals(1, abrechnungRepository.count());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void zusammenspielNachherVorher() {
        SachKontoProvider provider = sachKontoProvider();

        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        Abrechnung n�chsteAbrechnung = abrechnung
                .createOrGetN�chsteAbrechnung(provider);
        Optional<Abrechnung> vorkerigeDerN�chstenAbrechnung = n�chsteAbrechnung
                .getVorherigeAbrechnung(provider);
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertTrue(vorkerigeDerN�chstenAbrechnung.isPresent());
        assertEquals(abrechnung.getAbrechnungId(),
                vorkerigeDerN�chstenAbrechnung.get().getAbrechnungId());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void abgerechneteAbrechnung() {
        Mandant mandant = erzeugeMandant();
        Abrechnung abrechnung = erzeugeAbrechnung(mandant);
        abrechnung.setMj(new MonatJahr(3, 2018));
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        abrechnung.setTyp(AbrechnungsTyp.TEILABRECHNUNG);
        abrechnung = abrechnungRepository.save(abrechnung);
        Optional<Abrechnung> abgerechneteAbrechnung = mandant
                .getLetzteAbgerechneteAbrechnung(sachKontoProvider(),
                        new MonatJahr(3, 2018), AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertTrue(abgerechneteAbrechnung.isPresent());
    }

    @Test
    @Transactional("dbATransactionManager")
    public void neueAbrechnung() {
        SachKontoProvider provider = sachKontoProvider();

        Mandant mandant = mandantRepository.save(erzeugeMandant());
        Abrechnung neueAbrechnung = mandant.createNeueAbrechnung(provider,
                new MonatJahr(3, 2018), AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertTrue(neueAbrechnung != null);
        assertEquals(1, neueAbrechnung.getNummer());

        neueAbrechnung = mandant.createNeueAbrechnung(provider, new MonatJahr(
                3, 2018), AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertTrue(neueAbrechnung != null);
        assertEquals(2, neueAbrechnung.getNummer());

    }

}
