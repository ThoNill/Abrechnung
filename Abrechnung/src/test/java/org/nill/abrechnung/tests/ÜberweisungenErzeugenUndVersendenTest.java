package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.repositories.AusgangsDateiRepository;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.nill.zahlungen.actions.ÜberweisungsDatei;
import org.nill.zahlungen.values.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class ÜberweisungenErzeugenUndVersendenTest extends MitÜberweisungenTest {
    protected static final Logger log = LoggerFactory
            .getLogger(ÜberweisungenErzeugenUndVersendenTest.class);

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private ÜberweisungRepository überweisungRepository;

    @Autowired
    private AusgangsDateiRepository ausgangsDateiRepository;

    @Value("classpath:pain.001.003.03.xsd")
    Resource überweisungsXmlSchema;

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {

        überweisungRepository.deleteAll();
        ausgangsDateiRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Test
    public void aufträgeErzeugen() {
        Mandant mandant = erzeugeMandant();

        createÜberweisung("DE02500105170137075030", 5, mandant,
                überweisungRepository);
        createÜberweisung("DE02200505501015871393", 20, mandant,
                überweisungRepository);

        assertEquals(25, überweisungRepository.count());

        try {
            ÜberweisungsDatei manager = new ÜberweisungsDatei(
                    ausgangsDateiRepository, überweisungRepository, ".",
                    "Test", 1, new TypeReference(1, 1L));
            manager.markiereÜberweisungsDateien(10);
            manager.dateienMarkierenUndErstellen();
        } catch (Exception e) {
            log.error("Unerwarteter Fehler ", e);
            fail(e.getMessage());
        }

        assertEquals(3, ausgangsDateiRepository.count());

    }

}
