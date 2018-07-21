package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.actions.GebührenBerechnung;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundingContext.abrechnung.flow.payloads.GebührDefinitionPayload;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<GebührDefinitionPayload, BuchungAuftragPayload> {

    private AbrechnungsKonfigurator konfigurator;

    public BerechneBuchungsauftrag(AbrechnungsKonfigurator konfigurator) {
        super();
        this.konfigurator = konfigurator;
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            GebührDefinitionPayload payload) throws Exception {
        GebührenBerechnung berechnung = konfigurator
                .erzeugeGebührenBerechner(payload.getDefinition());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
