package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.allgemein.values.TypeReference;
import org.nill.zahlungen.actions.ÜberweisungsDatei;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestDbConfig.class })
public class ÜberweisungenErzeugenUndVersendenTest extends MitÜberweisungenTest {
    protected static final Logger log = LoggerFactory
            .getLogger(ÜberweisungenErzeugenUndVersendenTest.class);

    @Value("classpath:pain.001.003.03.xsd")
    Resource überweisungsXmlSchema;


    public IMandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Test
    public void aufträgeErzeugen() {
        IMandant mandant = erzeugeMandant();

        createÜberweisung("DE02500105170137075030", 5, mandant,
                überweisungRepository);
        createÜberweisung("DE02200505501015871393", 20, mandant,
                überweisungRepository);

        assertEquals(25, überweisungRepository.count());

        try {
            ÜberweisungsDatei manager = new ÜberweisungsDatei(umgebung()
                    , ".",
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
