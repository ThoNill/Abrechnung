package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.aufz�hlungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufz�hlungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufz�hlungen.RunStatus;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.helper.AbrechnungHelper;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.config.TestDbConfig.class })
public class AbrechnungsTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

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
        abrechnung.setJahr(2018);
        abrechnung.setMonat(4);
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
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        return helper.createOrGetN�chsteAbrechnung(abrechnung);
    }

    public Abrechnung naechsteAbrechnungMerfacherAufruf() {
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        helper.createOrGetN�chsteAbrechnung(abrechnung);
        helper.createOrGetN�chsteAbrechnung(abrechnung);
        return helper.createOrGetN�chsteAbrechnung(abrechnung);
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
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        return helper.getVorherigeAbrechnung(abrechnung);
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
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Abrechnung n�chsteAbrechnung = helper
                .createOrGetN�chsteAbrechnung(abrechnung);
        Optional<Abrechnung> vorkerigeDerN�chstenAbrechnung = helper
                .getVorherigeAbrechnung(n�chsteAbrechnung);
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
        abrechnung.setMonat(3);
        abrechnung.setJahr(2018);
        abrechnung.setStatus(AbrechnungsStatus.ABGERECHNET);
        abrechnung.setTyp(AbrechnungsTyp.TEILABRECHNUNG);
        abrechnung = abrechnungRepository.save(abrechnung);
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Optional<Abrechnung> abgerechneteAbrechnung = helper
                .getLetzteAbgerechneteAbrechnung(mandant, 3,2018,AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertTrue(abgerechneteAbrechnung.isPresent());
    }
    
    @Test
    @Transactional("dbATransactionManager")
    public void neueAbrechnung() {
        Mandant mandant = mandantRepository.save(erzeugeMandant());
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        Abrechnung neueAbrechnung = helper.createNeueAbrechnung(mandant, 3,2018,AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(1, abrechnungRepository.count());
        assertTrue(neueAbrechnung != null);
        assertEquals(1, neueAbrechnung.getNummer());
        
        neueAbrechnung = helper.createNeueAbrechnung(mandant, 3,2018,AbrechnungsTyp.TEILABRECHNUNG);
        assertEquals(1, mandantRepository.count());
        assertEquals(2, abrechnungRepository.count());
        assertTrue(neueAbrechnung!=null);
        assertEquals(2, neueAbrechnung.getNummer());
        
    }

}
