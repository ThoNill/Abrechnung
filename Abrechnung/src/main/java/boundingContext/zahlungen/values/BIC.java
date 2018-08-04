package boundingContext.zahlungen.values;

import lombok.Data;

@Data
public class BIC {
    private String bic;

    public BIC() {
        this("");
    }

    public BIC(String bic) {
        super();
        this.bic = bic;
    }

    @Override
    public String toString() {
        return bic;
    }

}
