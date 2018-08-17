package org.nill.abrechnung.flow.payloads;

import lombok.Value;

import org.nill.abrechnung.aufz�hlungen.AbrechnungsArt;
import org.nill.abrechnung.aufz�hlungen.AbrechnungsTyp;
import org.nill.allgemein.values.MonatJahr;

@Value
public class AufrufPayload {
    private AbrechnungsArt art;
    private long mandantId;
    private long abrechnungId;
    private MonatJahr mj;
    private AbrechnungsTyp typ;

}
