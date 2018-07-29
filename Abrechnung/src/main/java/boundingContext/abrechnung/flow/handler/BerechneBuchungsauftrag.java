package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.actions.Geb�hrenBerechnung;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.aufz�hlungen.SachKontoProvider;
import boundingContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundingContext.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

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
        Geb�hrenBerechnung berechnung = konfigurator
                .erzeugeGeb�hrenBerechner(payload.getDefinition(),sachKontoProvider,payload.getArt());
        BuchungsAuftrag<SachKonto> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
