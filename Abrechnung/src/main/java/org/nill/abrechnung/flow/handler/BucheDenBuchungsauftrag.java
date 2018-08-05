package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.abrechnung.repositories.BuchungRepository;
import org.nill.buchhaltung.eingang.EinBucher;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BucheDenBuchungsauftrag
        extends
        AbstractPayloadTransformer<BuchungAuftragPayload, BuchungAuftragPayload> {

    protected BuchungRepository buchungRepository;
    protected SachKontoProvider sachKontoProvider;
    protected AbrechnungRepository abrechnungRepository;

    public BucheDenBuchungsauftrag(SachKontoProvider sachKontoProvider,
            BuchungRepository buchungRepository,
            AbrechnungRepository abrechnungRepository) {
        super();
        this.sachKontoProvider = sachKontoProvider;
        this.buchungRepository = buchungRepository;
        this.abrechnungRepository = abrechnungRepository;
    }

    private EinBucher erzeugeEinbucher() {
        return new EinBucher(sachKontoProvider);
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            BuchungAuftragPayload payload) throws Exception {
        erzeugeEinbucher().erzeugeDifferenzBuchung(payload.getAuftrag(),
                payload.getAbrechnung());
        return payload;
    }

}
