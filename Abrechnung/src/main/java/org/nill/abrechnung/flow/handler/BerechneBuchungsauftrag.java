package org.nill.abrechnung.flow.handler;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.flow.payloads.BuchungAuftragPayload;
import org.nill.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import org.nill.abrechnung.interfaces.AbrechnungsKonfigurator;
import org.nill.abrechnung.interfaces.IGeb�hrBerechnung;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<Geb�hrDefinitionPayload, BuchungAuftragPayload> {

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
            Geb�hrDefinitionPayload payload) throws Exception {
        IGeb�hrBerechnung berechnung = konfigurator.erzeugeGeb�hrenBerechner(
                payload.getDefinition(), umgebung, payload.getArt());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
