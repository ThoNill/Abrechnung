package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BucheDenBuchungsauftrag
        extends
        AbstractPayloadTransformer<BuchungAuftragPayload, BuchungAuftragPayload> {

    protected Umgebung umgebung;

    public BucheDenBuchungsauftrag(Umgebung umgebung) {
        super();
        this.umgebung = umgebung;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(umgebung);
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            BuchungAuftragPayload payload) throws Exception {
        erzeugeEinbucher().erzeugeDifferenzBuchung(payload.getAuftrag(),
                payload.getAbrechnung());

        IAbrechnung abrechnung = umgebung.getAbrechnungRepository()
                .saveIAbrechnung(payload.getAbrechnung());
        return new BuchungAuftragPayload(abrechnung, payload.getMandant(),
                payload.getArt(), payload.getDefinition(), payload.getAuftrag());
    }

}
