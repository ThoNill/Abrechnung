package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.flow.payloads.AuszahlungPayload;
import org.nill.zahlungen.actions.ÜberweisungsDatei;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class MarkiereÜberweiungsDateien extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected ÜberweisungsDatei manager;
    protected int count;

    public MarkiereÜberweiungsDateien(ÜberweisungsDatei manager, int count) {
        super();
        this.manager = manager;
        this.count = count;
    }

    @Override
    protected AuszahlungPayload transformPayload(AuszahlungPayload payload)
            throws Exception {
        manager.markiereÜberweisungsDateien(count);
        return payload;
    }

}
