package app.entities;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
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
@Table(name = "KONTOBEWEGUNG")
@SequenceGenerator(name = "KONTOBEWEGUNG_SEQ", sequenceName = "KONTOBEWEGUNG_SEQ")
public class KontoBewegung extends KontoBewegungUser {

    public KontoBewegung() {
        super();
    }

    @Override
    @Basic
    @Column(name = "KONTOBEWEGUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KONTOBEWEGUNG_SEQ")
    public java.lang.Long getKontoBewegungId() {
        return super.getKontoBewegungId();
    };

    @Override
    @Basic
    @Column(name = "ART")
    public int getArt() {
        return super.getArt();
    };

    @Override
    @Basic
    @Column(name = "KONTONR")
    public int getKontoNr() {
        return super.getKontoNr();
    };

    @Override
    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = betrag.GeldKonverter.class)
    public javax.money.MonetaryAmount getBetrag() {
        return super.getBetrag();
    };

    @Override
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "BuchungId")
    public Buchung getBuchung() {
        return super.getBuchung();
    };

}
