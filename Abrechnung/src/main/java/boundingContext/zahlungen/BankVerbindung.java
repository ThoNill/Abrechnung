package boundingContext.zahlungen;

import ddd.Value;

public class BankVerbindung implements Value {
    private IBAN iban;
    private BIC bic;

    public BankVerbindung(IBAN iban, BIC bic) {
        super();
        this.iban = iban;
        this.bic = bic;
    }

    public IBAN getIban() {
        return iban;
    }

    public void setIban(IBAN iban) {
        this.iban = iban;
    }
}
