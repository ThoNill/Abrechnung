package org.nill.abrechnung.flow.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

public class Geb�hrDefinitionSplitter extends AbstractMessageSplitter {
    AtomicInteger correlationId = new AtomicInteger();

    @Override
    protected List<Message> splitMessage(Message<?> message) {
        AbrechnungPayload param = (AbrechnungPayload) message.getPayload();
        Mandant mandant = param.getMandant();
        int id = correlationId.addAndGet(1);
        List<Message> messages = new ArrayList<>();
        Set<GebuehrDefinition> liste = mandant.getGebuehrDefinitionen();
        int nr = 1;
        for (GebuehrDefinition gdef : liste) {

            Geb�hrDefinitionPayload gdParam = new Geb�hrDefinitionPayload(
                    param.getAbrechnung(), mandant, param.getArt(), gdef);
            MessageBuilder<Geb�hrDefinitionPayload> builder = MessageBuilder
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
