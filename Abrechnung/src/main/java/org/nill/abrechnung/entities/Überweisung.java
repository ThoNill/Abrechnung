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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.IZahlungsAuftrag;
import org.nill.abrechnung.interfaces.I�berweisung;
import org.nill.zahlungen.values.BankVerbindung;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "UEBERWEISUNG")
@SequenceGenerator(name = "UEBERWEISUNG_SEQ", sequenceName = "UEBERWEISUNG_SEQ")
public class �berweisung implements I�berweisung {

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
    @Convert(converter = org.nill.basiskomponenten.betrag.GeldKonverter.class)
    private MonetaryAmount betrag;

    @Basic
    private Date erstellt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "iban.iban", column = @Column(name = "von_iban")),
            @AttributeOverride(name = "bic.bic", column = @Column(name = "von_bic")),
            @AttributeOverride(name = "name", column = @Column(name = "von_name")) })
    private BankVerbindung von;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "iban.iban", column = @Column(name = "an_iban")),
            @AttributeOverride(name = "bic.bic", column = @Column(name = "an_bic")),
            @AttributeOverride(name = "sname", column = @Column(name = "an_name")) })
    private BankVerbindung an;

    @Basic
    private Date ausbezahlt;

    @Basic
    private Date best�tigt;

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
    private ZahlungsAuftrag zahlungsAuftrag;

    @Override
    public void setIAusgangsDatei(IAusgangsDatei ausgangsDatei) {
        setAusgangsDatei((AusgangsDatei) ausgangsDatei);
    }

    @Override
    public void setIMandat(IMandant mandant) {
        setMandant((Mandant) mandant);
    }

    @Override
    public void setIZahlungsAuftrag(IZahlungsAuftrag auftrag) {
        setZahlungsAuftrag((ZahlungsAuftrag) auftrag);
    }

}
