package boundingContext.zahlungen.values;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.Data;
import ddd.Value;

@Data
public @Embeddable class BankVerbindung implements Value {
    @Convert(converter = boundingContext.zahlungen.values.JaxbIBANAdapter.class)
    @Column(name = "iban")
    private IBAN iban;

    @Convert(converter = boundingContext.zahlungen.values.JaxbBICAdapter.class)
    @Column(name = "bic")
    private BIC bic;

    private String name;

    public BankVerbindung() {
        this(new IBAN(), new BIC());
    }

    public BankVerbindung(IBAN iban, BIC bic) {
        super();
        this.iban = iban;
        this.bic = bic;
        this.name = "NN";
    }

}
