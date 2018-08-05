package org.nill.abrechnung.entities;

import java.util.Date;

import javax.money.MonetaryAmount;
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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.nill.zahlungen.values.BankVerbindung;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "ZAHLUNGSAUFTRAG")
@SequenceGenerator(name = "ZAHLUNGSAUFTRAG_SEQ", sequenceName = "ZAHLUNGSAUFTRAG_SEQ")
public class ZahlungsAuftrag {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "ZAHLUNGSAUFTRAGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZAHLUNGSAUFTRAG_SEQ")
    private Long zahlungsAuftragsId;

    @Basic
    @Column(name = "BUCHUNGSART")
    private int buchungsart;

    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = org.nill.basiskomponenten.betrag.GeldKonverter.class)
    private MonetaryAmount betrag;

    @Basic
    @Column(name = "ZU_ZAHLEN_AM")
    private Date zuZahlenAm;

    @Basic
    @Column(name = "AUSBEZAHLT")
    private Date ausbezahlt;

    @Basic
    @Column(name = "STORNIERT")
    private Date storniert;

    @Basic
    @Column(name = "BESTAETIGT")
    private Date bestätigt;

    @Basic
    @Column(name = "VERWENDUNGSZWECK")
    private String verwendungszweck;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MandantId")
    private Mandant mandant;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AbrechnungId")
    private Abrechnung abrechnung;

    @Embedded
    private BankVerbindung bank;

}
