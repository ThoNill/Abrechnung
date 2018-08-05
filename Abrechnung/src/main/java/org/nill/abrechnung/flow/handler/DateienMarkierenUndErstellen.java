package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.flow.payloads.AuszahlungPayload;
import org.nill.zahlungen.actions.ÜberweisungsDatei;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class DateienMarkierenUndErstellen extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected ÜberweisungsDatei manager;

    public DateienMarkierenUndErstellen(ÜberweisungsDatei manager) {
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
