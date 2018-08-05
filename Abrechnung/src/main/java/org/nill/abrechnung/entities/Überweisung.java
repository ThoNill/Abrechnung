package org.nill.abrechnung.entities;

import java.util.Date;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
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
@Table(name = "UEBERWEISUNG")
@SequenceGenerator(name = "UEBERWEISUNG_SEQ", sequenceName = "UEBERWEISUNG_SEQ")
public class Überweisung {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "UEBERWEISUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UEBERWEISUNG_SEQ")
    private long ueberweisungsId;

    @Basic
    @Column(name = "BUCHUNGSART")
    private int buchungsart;

    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = betrag.GeldKonverter.class)
    private MonetaryAmount betrag;

    @Basic
    private Date erstellt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "iban", column = @Column(name = "von_iban")),
            @AttributeOverride(name = "bic", column = @Column(name = "von_bic")),
            @AttributeOverride(name = "name", column = @Column(name = "von_name")) })
    private BankVerbindung von;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "iban", column = @Column(name = "an_iban")),
            @AttributeOverride(name = "bic", column = @Column(name = "an_bic")),
            @AttributeOverride(name = "name", column = @Column(name = "an_name")) })
    private BankVerbindung an;

    @Basic
    private Date ausbezahlt;

    @Basic
    private Date bestätigt;

    @Basic
    private String verwendungszweck;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AusgangsDateiId")
    private AusgangsDatei ausgangsDatei;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MandantId")
    private Mandant mandant;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ZahlungsAuftragId")
    private ZahlungsAuftrag auftrag;

}
