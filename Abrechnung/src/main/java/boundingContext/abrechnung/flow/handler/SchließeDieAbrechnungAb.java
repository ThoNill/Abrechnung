package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;

public class SchließeDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {
    private SachKontoProvider sachKontoProvider;

    public SchließeDieAbrechnungAb(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    protected AbrechnungPayload transformPayload(AbrechnungPayload payload)
            throws Exception {
        Abrechnung nächsteAbrechnung = payload.getAbrechnung().abschleißen(sachKontoProvider
                , 30,0.06,0.19);
        return payload;
    }
}
