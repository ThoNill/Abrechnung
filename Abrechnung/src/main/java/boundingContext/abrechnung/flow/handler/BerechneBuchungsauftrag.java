package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufz�hlungen.Position;
import boundingContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundingContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundingContext.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import boundingContext.abrechnung.helper.Geb�hrenBerechnung;
import boundingContext.abrechnung.helper.TestAbrechnungsKonfigurator;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<Geb�hrDefinitionPayload, BuchungAuftragPayload> {

    private LeistungRepository leistungRepository;

    public BerechneBuchungsauftrag(LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            Geb�hrDefinitionPayload payload) throws Exception {
        AbrechnungsKonfigurator konfigurator = new TestAbrechnungsKonfigurator(
                leistungRepository);
        Geb�hrenBerechnung berechnung = konfigurator
                .erzeugeGeb�hrenBerechner(payload.getDefinition());
        BuchungsAuftrag<Position> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
