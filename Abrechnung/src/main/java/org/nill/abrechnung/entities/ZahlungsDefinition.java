package org.nill.abrechnung.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.nill.zahlungen.values.BankVerbindung;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ZAHLUNGDEFINITION")
@SequenceGenerator(name = "ZAHLUNGDEFINITION_SEQ", sequenceName = "ZAHLUNGDEFINITION_SEQ")
public class ZahlungsDefinition {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "ZAHLUNGDEFINITIONID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZAHLUNGDEFINITION_SEQ")
    private long zahlungsDefinitionId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MandantId")
    private Mandant mandant;

    @Basic
    @Column(name = "BUCHUNGSART")
    private int buchungsart;

    @Basic
    @Column(name = "PROZENTSATZ")
    private double prozentSatz;

    @ToString.Include
    @Basic
    @Column(name = "ZEITART")
    private int zeitArt;

    @ToString.Include
    @Basic
    @Column(name = "TAG")
    private int tag;

    @Embedded
    private BankVerbindung bank;

    public Date berechneAuszahlungsTernin(Date ausgangsTermin) {
        int tage = getTag();
        Instant instant = ausgangsTermin.toInstant();
        instant.plusSeconds(Duration.ofDays(tage).getSeconds());
        return new Date(instant.toEpochMilli());
    }

}
