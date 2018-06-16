package tests.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import tests.AbrechnungsKonfigurator;
import tests.GebührenBerechnung;
import tests.Position;
import tests.TestAbrechnungsKonfigurator;
import tests.flow.payloads.BuchungAuftragPayload;
import tests.flow.payloads.GebührDefinitionPayload;
import app.repositories.LeistungRepository;
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
