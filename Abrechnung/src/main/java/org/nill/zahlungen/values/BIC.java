package org.nill.zahlungen.values;

import javax.persistence.Embeddable;

import lombok.Data;

@Data
public @Embeddable class BIC {
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
