package org.nill.abrechnung.flow.payloads;

import lombok.Getter;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGeb�hrDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.buchhaltung.eingang.BuchungsAuftrag;

@Getter
public class BuchungAuftragPayload extends Geb�hrDefinitionPayload {
    private BuchungsAuftrag<SachKonto> auftrag;

    public BuchungAuftragPayload(IAbrechnung abrechnung, IMandant mandant,
            AbrechnungsArt art, IGeb�hrDefinition definition,
            BuchungsAuftrag<SachKonto> auftrag) {
        super(abrechnung, mandant, art, definition);
        this.auftrag = auftrag;
    }

}
