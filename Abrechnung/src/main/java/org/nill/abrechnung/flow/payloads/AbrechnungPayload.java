package org.nill.abrechnung.flow.payloads;

import javax.validation.constraints.NotNull;

import lombok.Getter;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IMandant;

@Getter
public class AbrechnungPayload {
    private IAbrechnung abrechnung;
    private IMandant mandant;
    private AbrechnungsArt art;

    public AbrechnungPayload(@NotNull IAbrechnung abrechnung,
            @NotNull IMandant mandant, AbrechnungsArt art) {
        super();
        this.abrechnung = abrechnung;
        this.mandant = mandant;
        this.art = art;
    }
}
