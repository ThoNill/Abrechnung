package tests.flow.payloads;

import tests.Position;
import app.entities.Abrechnung;
import app.entities.GebuehrDefinition;
import app.entities.Mandant;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

public class BuchungAuftragPayload extends GebührDefinitionPayload {
    private BuchungsAuftrag<Position> auftrag;

    public BuchungAuftragPayload(Abrechnung abrechnung, Mandant mandant,
            AbrechnungsArt art, GebuehrDefinition definition,
            BuchungsAuftrag<Position> auftrag) {
        super(abrechnung, mandant, art, definition);
        this.auftrag = auftrag;
    }

    public BuchungsAuftrag<Position> getAuftrag() {
        return auftrag;
    }

}
