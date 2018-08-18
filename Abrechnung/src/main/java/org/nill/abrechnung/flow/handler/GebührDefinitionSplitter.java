package org.nill.abrechnung.flow.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.flow.payloads.GebührDefinitionPayload;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.Umgebung;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class GebührDefinitionSplitter extends AbstractMessageSplitter {
    AtomicInteger correlationId = new AtomicInteger();
    Umgebung provider;
    
    
    public GebührDefinitionSplitter(Umgebung provider) {
        super();
        this.provider = provider;
    }


    @Override
    protected List<Message> splitMessage(Message<?> message) {
        AbrechnungPayload param = (AbrechnungPayload) message.getPayload();
        IMandant mandant = param.getMandant();
        int id = correlationId.addAndGet(1);
        List<Message> messages = new ArrayList<>();
        Set<? extends IGebührDefinition> liste = mandant.getGebuehrDefinitionen();
        int nr = 1;
        IAbrechnung abrechnung = param.getAbrechnung();
        for (IGebührDefinition gdef : liste) {

            GebührDefinitionPayload gdParam = new GebührDefinitionPayload(abrechnung
                    , mandant, param.getArt(), gdef);
            MessageBuilder<GebührDefinitionPayload> builder = MessageBuilder
                    .withPayload(gdParam);
            builder.setHeaderIfAbsent("inDatenbank", Boolean.TRUE);
            builder.setCorrelationId(id);
            builder.setSequenceNumber(nr);
            builder.setSequenceSize(liste.size());

            messages.add(builder.build());
            nr++;
        }
        
        return messages;
    }

}
