package boundingContext.abrechnung.entities;

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

import boundingContext.zahlungen.values.BankVerbindung;

@Entity
@Table(name = "ZAHLUNGDEFINITION")
@SequenceGenerator(name = "ZAHLUNGDEFINITION_SEQ", sequenceName = "ZAHLUNGDEFINITION_SEQ")
public class ZahlungsDefinition {
    private long zahlungsDefinitionId;
    private Mandant mandant;

    private int buchungsart;

    private double prozentSatz;
    private int zeitArt;
    private int tag;
    private BankVerbindung bank;

    @Basic
    @Column(name = "ZAHLUNGDEFINITIONID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZAHLUNGDEFINITION_SEQ")
    public long getZahlungsDefinitionId() {
        return zahlungsDefinitionId;
    }

    public void setZahlungsDefinitionId(long zahlungsDefinitionId) {
        this.zahlungsDefinitionId = zahlungsDefinitionId;
    }

    @Basic
    @Column(name = "BUCHUNGSART")
    public int getBuchungsart() {
        return buchungsart;
    }

    public void setBuchungsart(int buchungsart) {
        this.buchungsart = buchungsart;
    }

    @Basic
    @Column(name = "PROZENTSATZ")
    public double getProzentSatz() {
        return prozentSatz;
    }

    public void setProzentSatz(double prozentSatz) {
        this.prozentSatz = prozentSatz;
    }

    @Basic
    @Column(name = "ZEITART")
    public int getZeitArt() {
        return zeitArt;
    }

    public void setZeitArt(int zeitArt) {
        this.zeitArt = zeitArt;
    }

    @Basic
    @Column(name = "TAG")
    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MandantId")
    public Mandant getMandant() {
        return mandant;
    }

    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    }

    public Date berechneAuszahlungsTernin(Date ausgangsTermin) {
        int tage = getTag();
        Instant instant = ausgangsTermin.toInstant();
        instant.plusSeconds(Duration.ofDays(tage).getSeconds());
        return new Date(instant.toEpochMilli());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + (int) (zahlungsDefinitionId ^ (zahlungsDefinitionId >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ZahlungsDefinition other = (ZahlungsDefinition) obj;
        if (zahlungsDefinitionId != other.zahlungsDefinitionId)
            return false;
        return true;
    }

    @Embedded
    public BankVerbindung getBank() {
        return bank;
    }

    public void setBank(BankVerbindung bank) {
        this.bank = bank;
    };

}
