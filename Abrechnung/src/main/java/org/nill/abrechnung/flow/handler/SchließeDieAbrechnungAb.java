package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.SachKontoProvider;
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
        IAbrechnung abrechnung = payload.getAbrechnung();
        abrechnung.abschleiﬂen(sachKontoProvider);
        return payload;
    }
}
