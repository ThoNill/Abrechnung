package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.aufz‰hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

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
