package org.nill.abrechnung.flow.payloads;

import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.zahlungen.values.MonatJahr;

import lombok.Value;

@Value
public class AufrufPayload {
    private AbrechnungsArt art;
    private long mandantId;
    private long abrechnungId;
    private MonatJahr mj;
    private AbrechnungsTyp typ;

}
