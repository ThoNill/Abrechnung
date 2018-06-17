package boundaryContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundaryContext.abrechnung.flow.AbrechnungsKonfigurator;
import boundaryContext.abrechnung.flow.payloads.BuchungAuftragPayload;
import boundaryContext.abrechnung.flow.payloads.Geb�hrDefinitionPayload;
import boundaryContext.abrechnung.helper.Geb�hrenBerechnung;
import boundaryContext.abrechnung.helper.TestAbrechnungsKonfigurator;
import boundaryContext.abrechnung.repositories.LeistungRepository;
import boundingContext.abrechnung.aufz�hlungen.Position;
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
