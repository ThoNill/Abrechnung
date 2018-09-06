package org.nill.abrechnung.tests;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.flow.payloads.AuszahlungPayload;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.repositories.MandantRepository;
import org.nill.abrechnung.repositories.ÜberweisungRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.integration.dsl.StandardIntegrationFlow;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableIntegration
@SpringBootTest(classes = { org.nill.abrechnung.tests.config.TestConfig.class,
        org.nill.abrechnung.tests.config.TestDbConfig.class,
        org.nill.abrechnung.flow.AuszahlungFlow.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class AuszahlungTest extends MitÜberweisungenTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private ÜberweisungRepository überweisungRepository;

    @Autowired
    @Qualifier("auszahlungChannel")
    public DirectChannel auszahlungChannel;

    @Autowired
    @Qualifier("auszahlungFlowEndChannel")
    public DirectChannel auszahlungFlowEndChannel;

    @Autowired
    @Qualifier("auszahlungFlow")
    StandardIntegrationFlow flow;

    @Override
    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        mandantRepository.deleteAll();
        überweisungRepository.deleteAll();
    }

    public IMandant erzeugeMandant() {
        return mandantRepository.save(new Mandant());
    }

    @Test
    public void normalerAblauf() {

        auszahlungFlowEndChannel
                .addInterceptor(new ChannelInterceptorAdapter() {
                    @Override
                    public void postSend(Message message,
                            MessageChannel channel, boolean sent) {
                        assertEquals(1, mandantRepository.count());
                        assertEquals(0, abrechnungRepository.count());
                        assertEquals(25, überweisungRepository.count());
                        assertEquals(3, ausgangsDateiRepository.count());
                    }
                });

        flow.start();

        IMandant mandant = erzeugeMandant();

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
