package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.flow.payloads.AuszahlungPayload;
import org.nill.zahlungen.actions.�berweisungsDatei;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class DateienMarkierenUndErstellen extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected �berweisungsDatei manager;

    public DateienMarkierenUndErstellen(�berweisungsDatei manager) {
        super();
        this.manager = manager;
    }

    @Override
    protected AuszahlungPayload transformPayload(AuszahlungPayload payload)
            throws Exception {
        manager.dateienMarkierenUndErstellen();
        return payload;
    }

}
