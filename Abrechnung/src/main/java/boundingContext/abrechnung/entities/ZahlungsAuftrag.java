package boundingContext.abrechnung.entities;

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

import boundingContext.zahlungen.values.BankVerbindung;

@Entity
@Table(name = "ZAHLUNGSAUFTRAG")
@SequenceGenerator(name = "ZAHLUNGSAUFTRAG_SEQ", sequenceName = "ZAHLUNGSAUFTRAG_SEQ")
public class ZahlungsAuftrag {

    public ZahlungsAuftrag() {
        super();
    }

    private Mandant mandant;
    private Abrechnung abrechnung;
    private Long zahlungsAuftragsId;
    private int buchungsart;

    private MonetaryAmount betrag;
    private Date zuZahlenAm;
    private BankVerbindung bank;
    private Date ausbezahlt;
    private Date storniert;
    private Date bestätigt;
    private String verwendungszweck;

    @Basic
    @Column(name = "ZAHLUNGSAUFTRAGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ZAHLUNGSAUFTRAG_SEQ")
    public Long getZahlungsAuftragsId() {
        return zahlungsAuftragsId;
    }

    public void setZahlungsAuftragsId(Long zahlungsAuftragsId) {
        this.zahlungsAuftragsId = zahlungsAuftragsId;
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
    @Column(name = "BETRAG")
    @Convert(converter = betrag.GeldKonverter.class)
    public MonetaryAmount getBetrag() {
        return betrag;
    }

    public void setBetrag(MonetaryAmount betrag) {
        this.betrag = betrag;
    }

    @Basic
    @Column(name = "AUSBEZAHLT")
    public Date getAusbezahlt() {
        return ausbezahlt;
    }

    public void setAusbezahlt(Date ausbezahlt) {
        this.ausbezahlt = ausbezahlt;
    }

    @Basic
    @Column(name = "BESTAETIGT")
    public Date getBestätigt() {
        return bestätigt;
    }

    public void setBestätigt(Date bestätigt) {
        this.bestätigt = bestätigt;
    }

    @Basic
    @Column(name = "VERWENDUNGSZWECK")
    public String getVerwendungszweck() {
        return verwendungszweck;
    }

    public void setVerwendungszweck(String verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
    }

    @Basic
    @Column(name = "ZU_ZAHLEN_AM")
    public Date getZuZahlenAm() {
        return zuZahlenAm;
    }

    public void setZuZahlenAm(Date zuZahlenAm) {
        this.zuZahlenAm = zuZahlenAm;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "MandantId")
    public Mandant getMandant() {
        return mandant;
    }

    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    }

    @Embedded
    public BankVerbindung getBank() {
        return bank;
    }

    public void setBank(BankVerbindung bank) {
        this.bank = bank;
    }

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AbrechnungId")
    public Abrechnung getAbrechnung() {
        return abrechnung;
    }

    public void setAbrechnung(Abrechnung abrechnung) {
        this.abrechnung = abrechnung;
    }

    @Basic
    @Column(name = "STORNIERT")
    public Date getStorniert() {
        return storniert;
    }

    public void setStorniert(Date storniert) {
        this.storniert = storniert;
    };

}
