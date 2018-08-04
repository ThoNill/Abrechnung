package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;

public class SchlieﬂeDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {
    private SachKontoProvider sachKontoProvider;

    public SchlieﬂeDieAbrechnungAb(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    protected AbrechnungPayload transformPayload(AbrechnungPayload payload)
            throws Exception {
        Abrechnung n‰chsteAbrechnung = payload.getAbrechnung().abschleiﬂen(
                sachKontoProvider, 30, 0.06, 0.19);
        return payload;
    }
}
