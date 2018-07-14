package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.flow.payloads.AuszahlungPayload;
import boundingContext.zahlungen.helper.‹berweisungenManager;

public class Markiere‹berweiungsDateien
        extends
        AbstractPayloadTransformer<AuszahlungPayload,AuszahlungPayload> {

    protected ‹berweisungenManager manager;
    protected int count;

    public Markiere‹berweiungsDateien(‹berweisungenManager manager,int count) {
        super();
        this.manager = manager;
        this.count = count;
    }

    @Override
    protected AuszahlungPayload transformPayload(
            AuszahlungPayload payload) throws Exception {
        manager.markiere‹berweisungsDateien(count);
        return payload;
    }

}
