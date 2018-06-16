package tests.flow.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.integration.splitter.AbstractMessageSplitter;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;

import tests.flow.payloads.AbrechnungPayload;
import tests.flow.payloads.GebührDefinitionPayload;
import app.entities.GebuehrDefinition;
import app.entities.Mandant;

public class GebührDefinitionSplitter extends AbstractMessageSplitter {
    AtomicInteger correlationId = new AtomicInteger();

    @Override
    protected List<Message> splitMessage(Message<?> message) {
        AbrechnungPayload param = (AbrechnungPayload) message.getPayload();
        Mandant mandant = param.getMandant();
        int id = correlationId.addAndGet(1);
        List<Message> messages = new ArrayList<>();
        List<GebuehrDefinition> liste = mandant.getGebuehrDefinitionen();
        int nr = 1;
        for (GebuehrDefinition gdef : liste) {

            GebührDefinitionPayload gdParam = new GebührDefinitionPayload(
                    param.getAbrechnung(), mandant, param.getArt(), gdef);
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
