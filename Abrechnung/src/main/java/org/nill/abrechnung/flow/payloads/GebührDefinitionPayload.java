package org.nill.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import lombok.Getter;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;

@Getter
public class GebührDefinitionPayload extends AbrechnungPayload {
    private IGebührDefinition definition;

    public GebührDefinitionPayload(@NotNull IAbrechnung abrechnung,
            @NotNull IMandant mandant, AbrechnungsArt art,
            IGebührDefinition definition) {
        super(abrechnung, mandant, art);
        this.definition = definition;
    }

}
