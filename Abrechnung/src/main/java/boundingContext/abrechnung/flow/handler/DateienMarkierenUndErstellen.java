package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.zahlungen.actions.ÜberweisungsDatei;

public class DateienMarkierenUndErstellen
        extends
        AbstractPayloadTransformer<AuszahlungPayload, AuszahlungPayload> {

    protected ÜberweisungsDatei manager;

    public DateienMarkierenUndErstellen(ÜberweisungsDatei manager) {
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
