package tests.flow.handler;

import java.util.Collection;

import org.springframework.integration.annotation.Aggregator;
import org.springframework.messaging.Message;

import tests.flow.payloads.AbrechnungPayload;
import tests.flow.payloads.BuchungAuftragPayload;

public class GebührDefinitionAggregator {

    @Aggregator
    public AbrechnungPayload aggregate(
            Collection<Message<BuchungAuftragPayload>> products) {
        for (Message<BuchungAuftragPayload> msg : products) {
            BuchungAuftragPayload payload = msg.getPayload();
            return new AbrechnungPayload(payload.getAbrechnung(),
                    payload.getMandant(), payload.getArt());
        }
        return null;
    }
}