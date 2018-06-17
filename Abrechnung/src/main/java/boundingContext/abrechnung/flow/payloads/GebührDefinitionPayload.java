package boundingContext.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.GebuehrDefinition;
import boundingContext.abrechnung.entities.Mandant;

public class Geb�hrDefinitionPayload extends AbrechnungPayload {
    private GebuehrDefinition definition;

    public Geb�hrDefinitionPayload(@NotNull Abrechnung abrechnung,
            @NotNull Mandant mandant, AbrechnungsArt art,
            GebuehrDefinition definition) {
        super(abrechnung, mandant, art);
        this.definition = definition;
    }

    public GebuehrDefinition getDefinition() {
        return definition;
    }
}
