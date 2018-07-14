package boundingContext.abrechnung.flow.payloads;

import lombok.Getter;
import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;

@Getter
public class BuchungAuftragPayload extends Geb�hrDefinitionPayload {
    private BuchungsAuftrag<SachKonto> auftrag;

    public BuchungAuftragPayload(Abrechnung abrechnung, Mandant mandant,
            AbrechnungsArt art, GebuehrDefinition definition,
            BuchungsAuftrag<SachKonto> auftrag) {
        super(abrechnung, mandant, art, definition);
        this.auftrag = auftrag;
    }

}
