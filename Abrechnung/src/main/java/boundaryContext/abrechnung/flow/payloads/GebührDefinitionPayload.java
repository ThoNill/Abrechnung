package boundaryContext.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.entities.GebuehrDefinition;
import boundaryContext.abrechnung.entities.Mandant;

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
