package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.zahlungen.helper.�berweisungenManager;

public class DateienMarkierenUndErstellen
        extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected �berweisungenManager manager;

    public DateienMarkierenUndErstellen(�berweisungenManager manager) {
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
