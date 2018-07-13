package boundingContext.zahlungen.values;

import lombok.Data;

@Data
public class IBAN {
    private String iban;

    public IBAN() {
        this("");
    }
    
    public IBAN(String iban) {
        super();
        this.iban = iban;
    }
    
    public String toString() {
        return iban;
    }
}
