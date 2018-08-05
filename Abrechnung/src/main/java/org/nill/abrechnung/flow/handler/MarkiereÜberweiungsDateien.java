package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.flow.payloads.AuszahlungPayload;
import org.nill.zahlungen.actions.‹berweisungsDatei;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class Markiere‹berweiungsDateien extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected ‹berweisungsDatei manager;
    protected int count;

    public Markiere‹berweiungsDateien(‹berweisungsDatei manager, int count) {
        super();
        this.manager = manager;
        this.count = count;
    }

    @Override
    protected AuszahlungPayload transformPayload(AuszahlungPayload payload)
            throws Exception {
        manager.markiere‹berweisungsDateien(count);
        return payload;
    }

}
