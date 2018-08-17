package org.nill.abrechnung.tests;

import org.junit.After;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.aufzählungen.BuchungsArt;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebührDefinition;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.flow.payloads.AufrufPayload;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.SachKontoProvider;
import org.nill.allgemein.values.MonatJahr;
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
        org.nill.abrechnung.flow.AbrechnungFlow.class })
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class IntegrationTest extends AbrechnungBasisTest {

    @Override
    @Before
    @After
    @Transactional("dbATransactionManager")
    public void clear() {
        super.clear();
    }

    public IMandant erzeugeMandant() {
        Mandant mandant = mandantRepository.save(new Mandant());
        SachKontoProvider sachKontoProvider = sachKontoProvider();

        GebührDefinition gebührDefinition = new GebührDefinition();
        gebührDefinition.setArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setKontoNr(sachKontoProvider.GEBÜHR().ordinal());
        gebührDefinition.setGebührArt(1);
        gebührDefinition.setDatenArt(1);
        gebührDefinition.setParameter(0.06);
        gebührDefinition.setMwstKonto(sachKontoProvider.MWST().ordinal());
        gebührDefinition.setMwstSatz(0.19);
        gebührDefinition.setBuchungsArt(BuchungsArt.TESTBUCHUNG);
        gebührDefinition.setBuchungstext("Testbuchung");
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
    @Qualifier("abrechnungsFlowEndChannel")
    public DirectChannel abrechnungsFlowEndChannel;
    
    @Autowired
    @Qualifier("abrechnungFlow")
    StandardIntegrationFlow flow;

    @Test
    @Transactional("dbATransactionManager")
    public void normalerAblauf() {
       fülleParameter("30");
       
        abrechnungsFlowEndChannel.addInterceptor(new ChannelInterceptorAdapter() {
            @Override
            public void postSend(Message message, MessageChannel channel,
                    boolean sent) {
                assertEquals(1,mandantRepository.count());
                assertEquals(2,abrechnungRepository.count());
            }
        });
        flow.start();

        IMandant mandant = erzeugeMandant();

        AufrufPayload aufruf = new AufrufPayload(AbrechnungsArt.NEU,
                mandant.getMandantId(), 0, new MonatJahr(2, 2018),
                AbrechnungsTyp.TEILABRECHNUNG);

        Message<AufrufPayload> message = MessageBuilder.withPayload(aufruf)
                .setHeader("foo", "foo").setHeader("bar", "bar").build();
        parameterChannel.send(message);
        
        
    }
}
