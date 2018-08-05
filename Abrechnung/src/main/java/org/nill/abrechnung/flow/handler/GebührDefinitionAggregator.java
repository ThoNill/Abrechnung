package org.nill.abrechnung.flow.handler;

import java.util.Collection;

import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.springframework.integration.annotation.Aggregator;
import org.springframework.messaging.Message;

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