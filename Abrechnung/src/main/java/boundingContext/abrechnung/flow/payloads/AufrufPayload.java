package boundingContext.abrechnung.flow.payloads;

import lombok.Value;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.zahlungen.values.MonatJahr;

@Value
public class AufrufPayload {
    private AbrechnungsArt art;
    private long mandantId;
    private long abrechnungId;
    private MonatJahr mj;
    private AbrechnungsTyp typ;

 
}
