package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IGeb�hrBerechnung;
import org.nill.abrechnung.interfaces.SachKontoProvider;
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
        System.out.println("BerechneBuchungsauftrag start");
        IGeb�hrBerechnung berechnung = konfigurator.erzeugeGeb�hrenBerechner(
                payload.getDefinition(), sachKontoProvider, payload.getArt());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        System.out.println("BerechneBuchungsauftrag stop");
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
