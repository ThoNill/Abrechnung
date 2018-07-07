package boundingContext.zahlungen;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import ddd.Value;

public @Embeddable class BankVerbindung implements Value {
    private IBAN iban;
    private BIC bic;
    private String name;

    public BankVerbindung() {
        super();
    }
    
    public BankVerbindung(IBAN iban, BIC bic) {
        super();
        this.iban = iban;
        this.bic = bic;
        this.name = "NN";
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bic == null) ? 0 : bic.hashCode());
        result = prime * result + ((iban == null) ? 0 : iban.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        BankVerbindung other = (BankVerbindung) obj;
        if (bic == null) {
            if (other.bic != null)
                return false;
        } else if (!bic.equals(other.bic))
            return false;
        if (iban == null) {
            if (other.iban != null)
                return false;
        } else if (!iban.equals(other.iban))
            return false;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        return true;
    }
    
}
