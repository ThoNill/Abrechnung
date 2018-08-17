package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.actions.EinBucher;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IBuchungsRepository;
import org.nill.abrechnung.interfaces.SachKontoProvider;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BucheDenBuchungsauftrag
        extends
        AbstractPayloadTransformer<BuchungAuftragPayload, BuchungAuftragPayload> {

    protected SachKontoProvider sachKontoProvider;

    public BucheDenBuchungsauftrag(SachKontoProvider sachKontoProvider) {
        super();
        this.sachKontoProvider = sachKontoProvider;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider);
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            BuchungAuftragPayload payload) throws Exception {
        erzeugeEinbucher().erzeugeDifferenzBuchung(payload.getAuftrag(),payload.getAbrechnung());
      
        IAbrechnung abrechnung = sachKontoProvider.getAbrechnungRepository().saveIAbrechnung(payload.getAbrechnung());
        return new BuchungAuftragPayload(abrechnung,
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), payload.getAuftrag());
    }

}
