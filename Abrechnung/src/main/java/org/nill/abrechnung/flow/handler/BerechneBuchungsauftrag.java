package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.flow.payloads.GebührDefinitionPayload;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IGebührBerechnung;
import org.nill.abrechnung.interfaces.SachKontoProvider;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<GebührDefinitionPayload, BuchungAuftragPayload> {

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
            GebührDefinitionPayload payload) throws Exception {
        System.out.println("BerechneBuchungsauftrag start");
        IGebührBerechnung berechnung = konfigurator.erzeugeGebührenBerechner(
                payload.getDefinition(), sachKontoProvider, payload.getArt());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        System.out.println("BerechneBuchungsauftrag stop");
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
