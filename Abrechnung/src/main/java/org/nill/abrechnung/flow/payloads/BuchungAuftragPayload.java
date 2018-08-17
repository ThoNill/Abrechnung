package org.nill.abrechnung.flow.payloads;

import lombok.Getter;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

@Getter
public class BuchungAuftragPayload extends GebührDefinitionPayload {
    private BuchungsAuftrag<SachKonto> auftrag;

    public BuchungAuftragPayload(IAbrechnung abrechnung, IMandant mandant,
            AbrechnungsArt art, IGebührDefinition definition,
            BuchungsAuftrag<SachKonto> auftrag) {
        super(abrechnung, mandant, art, definition);
        this.auftrag = auftrag;
    }

}
