package org.nill.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import lombok.Getter;

import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.GebuehrDefinition;
import org.nill.abrechnung.entities.Mandant;

@Getter
public class GebührDefinitionPayload extends AbrechnungPayload {
    private GebuehrDefinition definition;

    public GebührDefinitionPayload(@NotNull Abrechnung abrechnung,
            @NotNull Mandant mandant, AbrechnungsArt art,
            GebuehrDefinition definition) {
        super(abrechnung, mandant, art);
        this.definition = definition;
    }

}
