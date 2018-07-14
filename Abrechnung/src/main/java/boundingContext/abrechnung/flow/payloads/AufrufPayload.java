package boundingContext.abrechnung.flow.payloads;

import lombok.Value;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;

@Value
public class AufrufPayload {
    private AbrechnungsArt art;
    private long mandantId;
    private long abrechnungId;
    private int monat;
    private int jahr;
    private AbrechnungsTyp typ;

 
}
