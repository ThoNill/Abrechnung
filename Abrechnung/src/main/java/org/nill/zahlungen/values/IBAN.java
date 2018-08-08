package org.nill.zahlungen.values;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
public @Embeddable class IBAN {
    private String iban;

    public IBAN() {
        this("");
    }

    public IBAN(String iban) {
        super();
        this.iban = iban;
    }

    @Override
    public String toString() {
        return iban;
    }
}
