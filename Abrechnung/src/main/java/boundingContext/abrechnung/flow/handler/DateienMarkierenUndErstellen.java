package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.zahlungen.actions.�berweisungsDatei;

public class DateienMarkierenUndErstellen
        extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected �berweisungsDatei manager;

    public DateienMarkierenUndErstellen(�berweisungsDatei manager) {
        super();
        this.manager = manager;
    }

    @Override
    protected AuszahlungPayload transformPayload(
            AuszahlungPayload payload) throws Exception {
        manager.dateienMarkierenUndErstellen();
        return payload;
    }

}
