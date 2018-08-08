package org.nill.abrechnung.values;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
@Embeddable
public class KontoBewegung {
    
    @Basic
    @Column(name = "ART")
    private int art;

    @Basic
    @Column(name = "KONTONR")
    private int kontoNr;

    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = org.nill.basiskomponenten.betrag.GeldKonverter.class)
    private javax.money.MonetaryAmount betrag;
}
