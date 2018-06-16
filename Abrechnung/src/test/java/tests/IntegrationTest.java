package tests;

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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import tests.flow.payloads.AbrechnungsArt;
import tests.flow.payloads.AufrufPayload;
import app.entities.GebuehrDefinition;
import app.entities.Mandant;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.GebührenDefinitionRepository;
import app.repositories.KontoBewegungRepository;
import app.repositories.LeistungRepository;
import app.repositories.MandantRepository;
import boundingContext.abrechnung.entities.AbrechnungsTyp;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableIntegration
@SpringBootTest(classes = { tests.db.TestDbConfig.class,
        tests.flow.AbrechnungFlow.class })
// @SpringBootTest(classes = { tests.flow.AbrechnungFlow.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest {

    @Autowired
    private MandantRepository mandantRepository;

    @Autowired
    private AbrechnungRepository abrechnungRepository;

    @Autowired
    private GebührenDefinitionRepository gebührenDefinitinRepository;

    @Autowired
    private LeistungRepository leistungRepository;

    @Autowired
    private BuchungRepository buchungRepository;

    @Autowired
    private KontoBewegungRepository kontoBewegungRepository;

    @Before
    @Transactional("dbATransactionManager")
    public void clear() {
        leistungRepository.deleteAll();
        kontoBewegungRepository.deleteAll();
        buchungRepository.deleteAll();
        abrechnungRepository.deleteAll();
        gebührenDefinitinRepository.deleteAll();
        mandantRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        Mandant mandant = mandantRepository.save(new Mandant());

        GebuehrDefinition gebührDefinition = new GebuehrDefinition();
        gebührDefinition.setArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setKontoNr(Position.GEBÜHR.ordinal());
        gebührDefinition.setGebührArt(1);
        gebührDefinition.setDatenArt(1);
        gebührDefinition.setParameter(0.06);
        gebührDefinition.setMwstKonto(Position.MWST.ordinal());
        gebührDefinition.setMwstSatz(0.19);
        gebührDefinition.setBuchungsArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setBuchungstext("Testbuchung");
        gebührDefinition.addMandant(mandant);
        gebührenDefinitinRepository.save(gebührDefinition);
        mandant.addGebuehrDefinitionen(gebührDefinition);
        return mandantRepository.save(mandant);
    }

    @Autowired
    @Qualifier("parameterChannel")
    public DirectChannel parameterChannel;

    @Autowired
    @Qualifier("mandantChannel")
    public DirectChannel mandantChannel;

    @Autowired
    StandardIntegrationFlow flow;

    @Test
    public void normalerAblauf() throws Exception {

        mandantChannel.addInterceptor(new ChannelInterceptorAdapter() {
            @Override
            public void postSend(Message message, MessageChannel channel,
                    boolean sent) {
                System.out.println("vor dem Splitten: " + message);
                super.postSend(message, channel, sent);
            }
        });
        flow.start();

        Mandant mandant = erzeugeMandant();
        // mandant.setMandantId(1l);

        AufrufPayload aufruf = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), 0, 2, 2018,
                AbrechnungsTyp.TEILABRECHNUNG);

        Message<AufrufPayload> message = MessageBuilder.withPayload(aufruf)
                .setHeader("foo", "foo").setHeader("bar", "bar").build();
        parameterChannel.send(message);
    }
    /*
     * 
     * val message = MessageBuilder.withPayload("Hello").setHeader("foo",
     * "foo").setHeader("bar", "bar").build messageFlow.send(message)
     */
}
