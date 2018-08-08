package org.nill.abrechnung.values;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import org.nill.zahlungen.values.BankVerbindung;

@Data
@EqualsAndHashCode
@ToString
@Embeddable
public class ZahlungsDefinition {
    
    
    @Basic
    @Column(name = "BUCHUNGSART")
    private int buchungsart;

    @Basic
    @Column(name = "PROZENTSATZ")
    private double prozentSatz;

    @Basic
    @Column(name = "ZEITART")
    private int zeitArt;

    @Basic
    @Column(name = "TAG")
    private int tag;

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name = "iban.iban", column = @Column(name = "bank_iban")),
        @AttributeOverride(name = "iban.bic", column = @Column(name = "bank_bic")),
        @AttributeOverride(name = "name", column = @Column(name = "bank_name")) })
    public BankVerbindung bank;

    public Date berechneAuszahlungsTernin(Date ausgangsTermin) {
        int tage = getTag();
        Instant instant = ausgangsTermin.toInstant();
        instant.plusSeconds(Duration.ofDays(tage).getSeconds());
        return new Date(instant.toEpochMilli());
    }

}
