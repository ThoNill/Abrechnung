package boundingContext.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;

@Getter
public class AbrechnungPayload {
    private Abrechnung abrechnung;
    private Mandant mandant;
    private AbrechnungsArt art;

    public AbrechnungPayload(@NotNull Abrechnung abrechnung,
            @NotNull Mandant mandant, AbrechnungsArt art) {
        super();
        this.abrechnung = abrechnung;
        this.mandant = mandant;
        this.art = art;
    }
}
