package org.nill.zahlungen.values;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Transient;

import lombok.Data;

import org.hibernate.annotations.Type;
import org.nill.basiskomponenten.ddd.Value;

@Data
public @Embeddable class BankVerbindung implements Value {
    
    @Embedded
    private IBAN iban;

    @Embedded
    private BIC bic;

    private String name;

    public BankVerbindung() {
        this(new IBAN(), new BIC());
    }

    public BankVerbindung(IBAN iban, BIC bic) {
        super();
        this.iban   = iban;
        this.bic = bic;
        this.name = "NN";
    }

}
