package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.zahlungen.helper.ÜberweisungenManager;

public class DateienMarkierenUndErstellen
        extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected ÜberweisungenManager manager;

    public DateienMarkierenUndErstellen(ÜberweisungenManager manager) {
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
