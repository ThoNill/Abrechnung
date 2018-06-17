package boundaryContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundaryContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundaryContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundaryContext.abrechnung.flow.payloads.GebührDefinitionPayload;
import boundaryContext.abrechnung.helper.GebührenBerechnung;
import boundaryContext.abrechnung.helper.TestAbrechnungsKonfigurator;
import boundaryContext.abrechnung.repositories.LeistungRepository;
import boundingContext.abrechnung.aufzählungen.Position;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

public class BerechneBuchungsauftrag
        extends
        AbstractPayloadTransformer<GebührDefinitionPayload, BuchungAuftragPayload> {

    private LeistungRepository leistungRepository;

    public BerechneBuchungsauftrag(LeistungRepository leistungRepository) {
        super();
        this.leistungRepository = leistungRepository;
    }

    @Override
    protected BuchungAuftragPayload transformPayload(
            GebührDefinitionPayload payload) throws Exception {
        AbrechnungsKonfigurator konfigurator = new TestAbrechnungsKonfigurator(
                leistungRepository);
        GebührenBerechnung berechnung = konfigurator
                .erzeugeGebührenBerechner(payload.getDefinition());
        BuchungsAuftrag<Position> auftrag = berechnung
                .markierenUndberechnen(payload.getAbrechnung());
        return new BuchungAuftragPayload(payload.getAbrechnung(),
                payload.getMandant(), payload.getArt(),
                payload.getDefinition(), auftrag);
    }

}
