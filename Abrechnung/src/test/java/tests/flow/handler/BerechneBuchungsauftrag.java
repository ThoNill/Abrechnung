package tests.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import tests.AbrechnungsKonfigurator;
import tests.Geb�hrenBerechnung;
import tests.Position;
import tests.TestAbrechnungsKonfigurator;
import tests.flow.payloads.BuchungAuftragPayload;
import tests.flow.payloads.Geb�hrDefinitionPayload;
import app.repositories.LeistungRepository;
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
