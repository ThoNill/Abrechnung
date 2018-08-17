package org.nill.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import lombok.Getter;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGeb�hrDefinition;
import org.nill.abrechnung.interfaces.IMandant;

@Getter
public class Geb�hrDefinitionPayload extends AbrechnungPayload {
    private IGeb�hrDefinition definition;

    public Geb�hrDefinitionPayload(@NotNull IAbrechnung abrechnung,
            @NotNull IMandant mandant, AbrechnungsArt art,
            IGeb�hrDefinition definition) {
        super(abrechnung, mandant, art);
        this.definition = definition;
    }

}
