package boundaryContext.abrechnung.entities;

import javax.money.MonetaryAmount;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "LEISTUNG")
@SequenceGenerator(name = "LEISTUNG_SEQ", sequenceName = "LEISTUNG_SEQ")
public class Leistung {
    private long leistungId;
    private MonetaryAmount betrag;
    private int art;
    private Abrechnung abrechnung;
    private Mandant mandant;

    public Leistung() {
        super();
    }

    @Basic
    @Column(name = "LEISTUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LEISTUNG_SEQ")
    public java.lang.Long getLeistungId() {
        return leistungId;
    };

    @Basic
    @Column(name = "ART")
    public int getArt() {
        return art;
    };

    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = betrag.GeldKonverter.class)
    public javax.money.MonetaryAmount getBetrag() {
        return betrag;
    }

    @ManyToOne()
    @JoinColumn(name = "AbrechnungId")
    public Abrechnung getAbrechnung() {
        return abrechnung;
    };

    public void setLeistungId(long leistungId) {
        this.leistungId = leistungId;
    }

    public void setBetrag(MonetaryAmount betrag) {
        this.betrag = betrag;
    }

    public void setArt(int art) {
        this.art = art;
    }

    public void setAbrechnung(Abrechnung abrechnung) {
        this.abrechnung = abrechnung;
    }

    @ManyToOne()
    @JoinColumn(name = "MandantId")
    public Mandant getMandant() {
        return mandant;
    }

    public void setMandant(Mandant mandant) {
        this.mandant = mandant;
    };

}
