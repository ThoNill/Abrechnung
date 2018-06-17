package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.helper.AbrechnungHelper;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;

@RunWith(SpringRunner.class)
// Class that run the tests
@SpringBootTest(classes = { tests.db.TestDbConfig.class })
public class AbrechnungsTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        abrechnungRepository.deleteAll();
        mandantRepository.deleteAll();
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
    public void nächsteAbrechnungTest() {
        Abrechnung abrechnung = naechsteAbrechnung();
        assertEquals(4, abrechnung.getNummer());
    }

    @Transactional("dbATransactionManager")
    public Abrechnung naechsteAbrechnung() {
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        return helper.createOrGetNächsteAbrechnung(abrechnung);
    }

    @Transactional("dbATransactionManager")
    public Abrechnung naechsteAbrechnungMefacherAufruf() {
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        helper.createOrGetNächsteAbrechnung(abrechnung);
        helper.createOrGetNächsteAbrechnung(abrechnung);
        return helper.createOrGetNächsteAbrechnung(abrechnung);
    }

    @Test
    public void nächsteAbrechnungMehrfacherAufrufTest() {
        Abrechnung abrechnung = naechsteAbrechnungMefacherAufruf();
        assertEquals(4, abrechnung.getNummer());
        assertEquals(2, abrechnungRepository.count());
    }

    @Transactional("dbATransactionManager")
    public Optional<Abrechnung> vorherigeAbrechnung() {
        Abrechnung abrechnung = erzeugeAbrechnung(erzeugeMandant());
        AbrechnungHelper helper = new AbrechnungHelper(abrechnungRepository);
        return helper.getVorherigeAbrechnung(abrechnung);
    }

    @Test
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
        Abrechnung nächsteAbrechnung = helper
                .createOrGetNächsteAbrechnung(abrechnung);
        Optional<Abrechnung> vorkerigeDerNächstenAbrechnung = helper
                .getVorherigeAbrechnung(nächsteAbrechnung);
        assertEquals(2, abrechnungRepository.count());
        assertTrue(vorkerigeDerNächstenAbrechnung.isPresent());
        assertEquals(abrechnung.getAbrechnungId(),
                vorkerigeDerNächstenAbrechnung.get().getAbrechnungId());
    }

}
