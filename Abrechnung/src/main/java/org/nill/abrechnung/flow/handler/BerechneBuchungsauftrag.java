package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.actions.Geb�hrenBerechnung;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<Geb�hrDefinitionPayload, BuchungAuftragPayload> {

    private AbrechnungsKonfigurator konfigurator;
    private SachKontoProvider sachKontoProvider;

    public BerechneBuchungsauftrag(AbrechnungsKonfigurator konfigurator,
            SachKontoProvider sachKontoProvider) {
        super();
        this.konfigurator = konfigurator;
        this.sachKontoProvider = sachKontoProvider;
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            Geb�hrDefinitionPayload payload) throws Exception {
        Geb�hrenBerechnung berechnung = konfigurator.erzeugeGeb�hrenBerechner(
                payload.getDefinition(), sachKontoProvider, payload.getArt());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
