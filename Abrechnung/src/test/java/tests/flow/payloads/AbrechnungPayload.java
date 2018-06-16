package tests.flow.payloads;

import javax.validation.constraints.NotNull;

import app.entities.Abrechnung;
import app.entities.Mandant;

public class AbrechnungPayload {
    private Mandant mandant;
    private AbrechnungsArt art;
    private Abrechnung abrechnung;

    public AbrechnungPayload(@NotNull Abrechnung abrechnung,
            @NotNull Mandant mandant, AbrechnungsArt art) {
        super();
        this.abrechnung = abrechnung;
        this.mandant = mandant;
        this.art = art;
    }

    public Abrechnung getAbrechnung() {
        return abrechnung;
    }

    public Mandant getMandant() {
        return mandant;
    }

    public AbrechnungsArt getArt() {
        return art;
    }
}
