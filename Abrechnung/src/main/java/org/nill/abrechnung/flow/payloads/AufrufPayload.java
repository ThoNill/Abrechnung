package org.nill.abrechnung.flow.payloads;

import lombok.Value;

import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.zahlungen.values.MonatJahr;

@Value
public class AufrufPayload {
    private AbrechnungsArt art;
    private long mandantId;
    private long abrechnungId;
    private MonatJahr mj;
    private AbrechnungsTyp typ;

}
