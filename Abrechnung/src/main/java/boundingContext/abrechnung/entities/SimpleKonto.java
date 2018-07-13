package boundingContext.abrechnung.entities;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import boundingContext.zahlungen.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)

public class SimpleKonto {
    private int art;
    private int nummer;
    private String bezeichnung;

    public SimpleKonto(int art, int nummer, String bezeichnung) {
        super();
        this.art = art;
        this.nummer = nummer;
        this.bezeichnung = bezeichnung;
    }


}
