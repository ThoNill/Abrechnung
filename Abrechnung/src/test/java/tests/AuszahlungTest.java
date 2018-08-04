package tests;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ÜberweisungRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableIntegration
@SpringBootTest(classes = { tests.config.TestConfig.class,
        tests.config.TestDbConfig.class,
        boundingContext.abrechnung.flow.AuszahlungFlow.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuszahlungTest extends MitÜberweisungenTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private ÜberweisungRepository überweisungRepository;

    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        mandantRepository.deleteAll();
        überweisungRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Autowired
    @Qualifier("auszahlungChannel")
    public DirectChannel auszahlungChannel;

    @Autowired
    @Qualifier("auszahlungFlow")
    StandardIntegrationFlow flow;

    @Test
    public void normalerAblauf() {

        flow.start();

        Mandant mandant = erzeugeMandant();

        createÜberweisung("DE02500105170137075030", 5, mandant,
                überweisungRepository);
        createÜberweisung("DE02200505501015871393", 20, mandant,
                überweisungRepository);

        AuszahlungPayload aufruf = new AuszahlungPayload();

        Message<AuszahlungPayload> message = MessageBuilder.withPayload(aufruf)
                .setHeader("foo", "foo").setHeader("bar", "bar").build();
        auszahlungChannel.send(message);
    }
}
