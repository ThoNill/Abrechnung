package boundingContext.abrechnung.entities;

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

import boundingContext.zahlungen.values.BankVerbindung;

@Entity
@Table(name = "UEBERWEISUNG")
@SequenceGenerator(name = "UEBERWEISUNG_SEQ", sequenceName = "UEBERWEISUNG_SEQ")
public class Überweisung  {

    public Überweisung() {
        super();
    }

    private Mandant mandant;
    private ZahlungsAuftrag auftrag;
    
    private long ueberweisungsId;
    private int buchungsart;
    private MonetaryAmount betrag;
    private Date erstellt;
    private BankVerbindung von;
    private BankVerbindung an;
    private Date ausbezahlt;
    private Date bestätigt;
    private String verwendungszweck;
    private AusgangsDatei ausgangsDatei;

    
    @Basic
    @Column(name = "UEBERWEISUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UEBERWEISUNG_SEQ")
    public long getUeberweisungsId() {
        return ueberweisungsId;
    }
    public void setUeberweisungsId(long ueberweisungsId) {
        this.ueberweisungsId = ueberweisungsId;
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
    public Date getErstellt() {
        return erstellt;
    }
 
    public void setErstellt(Date erstellt) {
        this.erstellt = erstellt;
    }
    
    @Basic
    public Date getAusbezahlt() {
        return ausbezahlt;
    }
    public void setAusbezahlt(Date ausbezahlt) {
        this.ausbezahlt = ausbezahlt;
    }
    
    @Basic
    public Date getBestätigt() {
        return bestätigt;
    }
    public void setBestätigt(Date bestätigt) {
        this.bestätigt = bestätigt;
    }
    
    
    @Basic
    public String getVerwendungszweck() {
        return verwendungszweck;
    }
    public void setVerwendungszweck(String verwendungszweck) {
        this.verwendungszweck = verwendungszweck;
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
    @AttributeOverrides({
        @AttributeOverride(name="iban", column=@Column(name="von_iban")),
        @AttributeOverride(name="bic", column=@Column(name="von_bic")),
        @AttributeOverride(name="name", column=@Column(name="von_name"))
    })
    public BankVerbindung getVon() {
        return von;
    }
    public void setVon(BankVerbindung von) {
        this.von = von;
    }

    @Embedded
    @AttributeOverrides({
        @AttributeOverride(name="iban", column=@Column(name="an_iban")),
        @AttributeOverride(name="bic", column=@Column(name="an_bic")),
        @AttributeOverride(name="name", column=@Column(name="an_name"))
    })
    public BankVerbindung getAn() {
        return an;
    }
    public void setAn(BankVerbindung an) {
        this.an = an;
    }


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "ZahlungsAuftragId")  
    public ZahlungsAuftrag getAuftrag() {
        return auftrag;
    }
    public void setAuftrag(ZahlungsAuftrag auftrag) {
        this.auftrag = auftrag;
    }
    
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AusgangsDateiId")  
    public AusgangsDatei getAusgangsDatei() {
        return ausgangsDatei;
    }
    
    public void setAusgangsDatei(AusgangsDatei ausgangsDatei) {
        this.ausgangsDatei = ausgangsDatei;
    }
   
}
