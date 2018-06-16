package tests.flow.payloads;

import javax.validation.constraints.NotNull;

import app.entities.Abrechnung;
import app.entities.GebuehrDefinition;
import app.entities.Mandant;

public class GebührDefinitionPayload extends AbrechnungPayload {
    private GebuehrDefinition definition;

    public GebührDefinitionPayload(@NotNull Abrechnung abrechnung,
            @NotNull Mandant mandant, AbrechnungsArt art,
            GebuehrDefinition definition) {
        super(abrechnung, mandant, art);
        this.definition = definition;
    }

    public GebuehrDefinition getDefinition() {
        return definition;
    }
}
