package org.nill.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;

import lombok.Getter;

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
