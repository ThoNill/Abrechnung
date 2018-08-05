package org.nill.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.entities.Mandant;

import lombok.Getter;

@Getter
public class Geb�hrDefinitionPayload extends AbrechnungPayload {
    private GebuehrDefinition definition;

    public Geb�hrDefinitionPayload(@NotNull Abrechnung abrechnung,
            @NotNull Mandant mandant, AbrechnungsArt art,
            GebuehrDefinition definition) {
        super(abrechnung, mandant, art);
        this.definition = definition;
    }

}
