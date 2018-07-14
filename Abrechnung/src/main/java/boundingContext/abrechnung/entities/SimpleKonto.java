package boundingContext.abrechnung.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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
