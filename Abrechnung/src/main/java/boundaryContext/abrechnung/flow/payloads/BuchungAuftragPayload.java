package boundaryContext.abrechnung.flow.payloads;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundaryContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.aufz�hlungen.Position;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

public class BuchungAuftragPayload extends Geb�hrDefinitionPayload {
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
