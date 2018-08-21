package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.flow.payloads.GebührDefinitionPayload;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IGebührBerechnung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<GebührDefinitionPayload, BuchungAuftragPayload> {

    private AbrechnungsKonfigurator konfigurator;
    private Umgebung umgebung;

    public BerechneBuchungsauftrag(AbrechnungsKonfigurator konfigurator,
            Umgebung umgebung) {
        super();
        this.konfigurator = konfigurator;
        this.umgebung = umgebung;
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            GebührDefinitionPayload payload) throws Exception {
        IGebührBerechnung berechnung = konfigurator.erzeugeGebührenBerechner(
                payload.getDefinition(), umgebung, payload.getArt());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
