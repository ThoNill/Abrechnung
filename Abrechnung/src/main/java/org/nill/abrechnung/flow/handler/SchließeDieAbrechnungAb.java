package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class Schlie�eDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {
    private Umgebung umgebung;

    public Schlie�eDieAbrechnungAb(Umgebung umgebung) {
        super();
        this.umgebung = umgebung;
    }

    @Override
    protected AbrechnungPayload transformPayload(AbrechnungPayload payload)
            throws Exception {
        IAbrechnung abrechnung = payload.getAbrechnung();
        abrechnung.abschlie�en(umgebung);
        return payload;
    }
}
