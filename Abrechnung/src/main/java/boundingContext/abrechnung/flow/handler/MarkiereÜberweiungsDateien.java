package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.zahlungen.helper.ÜberweisungenManager;

public class MarkiereÜberweiungsDateien
        extends
        AbstractPayloadTransformer<AuszahlungPayload,AuszahlungPayload> {

    protected ÜberweisungenManager manager;
    protected int count;

    public MarkiereÜberweiungsDateien(ÜberweisungenManager manager,int count) {
        super();
        this.manager = manager;
        this.count = count;
    }

    @Override
    protected AuszahlungPayload transformPayload(
            AuszahlungPayload payload) throws Exception {
        manager.markiereÜberweisungsDateien(count);
        return payload;
    }

}
