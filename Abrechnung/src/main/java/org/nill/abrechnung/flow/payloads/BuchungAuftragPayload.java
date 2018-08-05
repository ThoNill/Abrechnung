package org.nill.abrechnung.flow.payloads;

import lombok.Getter;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.entities.Mandant;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

@Getter
public class BuchungAuftragPayload extends GebührDefinitionPayload {
    private BuchungsAuftrag<SachKonto> auftrag;

    public BuchungAuftragPayload(Abrechnung abrechnung, Mandant mandant,
            AbrechnungsArt art, GebuehrDefinition definition,
            BuchungsAuftrag<SachKonto> auftrag) {
        super(abrechnung, mandant, art, definition);
        this.auftrag = auftrag;
    }

}
