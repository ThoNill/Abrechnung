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
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.BuchungsArt;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.flow.payloads.AufrufPayload;
import boundingContext.abrechnung.repositories.GebührenDefinitionRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@EnableIntegration
@SpringBootTest(classes = { tests.config.TestConfig.class,
        tests.config.TestDbConfig.class,
        boundingContext.abrechnung.flow.AbrechnungFlow.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest extends AbrechnungBasisTest{

    @Autowired
    private GebührenDefinitionRepository gebührenDefinitinRepository;

 
    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        super.clear();
        gebührenDefinitinRepository.deleteAll();
    }

    public Mandant erzeugeMandant() {
        Mandant mandant = mandantRepository.save(new Mandant());
        SachKontoProvider sachKontoProvider = sachKontoProvider();
        
        GebuehrDefinition gebührDefinition = new GebuehrDefinition();
        gebührDefinition.setArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setKontoNr(sachKontoProvider.GEBÜHR().ordinal());
        gebührDefinition.setGebührArt(1);
        gebührDefinition.setDatenArt(1);
        gebührDefinition.setParameter(0.06);
        gebührDefinition.setMwstKonto(sachKontoProvider.MWST().ordinal());
        gebührDefinition.setMwstSatz(0.19);
        gebührDefinition.setBuchungsArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setBuchungstext("Testbuchung");
        gebührenDefinitinRepository.save(gebührDefinition);
        gebührDefinition.addMandant(mandant);
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
    @Qualifier("abrechnungFlow")
    StandardIntegrationFlow flow;

    
    @Test
    @Transactional("dbATransactionManager")
    public void normalerAblauf()  {

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
     
        AufrufPayload aufruf = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), 0, 2, 2018,
                AbrechnungsTyp.TEILABRECHNUNG);

        Message<AufrufPayload> message = MessageBuilder.withPayload(aufruf)
                .setHeader("foo", "foo").setHeader("bar", "bar").build();
        parameterChannel.send(message);
    }
}
