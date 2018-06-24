package boundingContext.zahlungen;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import ddd.Value;

public @Embeddable class BankVerbindung implements Value {
    private IBAN iban;
    private BIC bic;

    public BankVerbindung() {
        super();
    }
    
    public BankVerbindung(IBAN iban, BIC bic) {
        super();
        this.iban = iban;
        this.bic = bic;
    }

    @Convert(converter = boundingContext.zahlungen.JaxbIBANAdapter.class)
    @Column(name = "iban")
    public IBAN getIban() {
        return iban;
    }

    public void setIban(IBAN iban) {
        this.iban = iban;
    }

    @Convert(converter = boundingContext.zahlungen.JaxbBICAdapter.class)
    @Column(name = "bic")
    public BIC getBic() {
        return bic;
    }

    public void setBic(BIC bic) {
        this.bic = bic;
    }
    
}
