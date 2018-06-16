package app.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import boundingContext.abrechnung.entities.AbrechnungsStatus;
import boundingContext.abrechnung.entities.AbrechnungsTyp;
import boundingContext.abrechnung.entities.RunStatus;

@Entity
@Table(name = "ABRECHNUNG")
@SequenceGenerator(name = "ABRECHNUNG_SEQ", sequenceName = "ABRECHNUNG_SEQ")
public class Abrechnung extends AbrechnungUser {

    public Abrechnung() {
        super();
    }

    @Override
    @Basic
    @Column(name = "ABRECHNUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ABRECHNUNG_SEQ")
    public java.lang.Long getAbrechnungId() {
        return super.getAbrechnungId();
    };

    @Override
    @Basic
    @Column(name = "MONAT")
    public int getMonat() {
        return super.getMonat();
    };

    @Override
    @Basic
    @Column(name = "JAHR")
    public int getJahr() {
        return super.getJahr();
    };

    @Override
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TYP")
    public AbrechnungsTyp getTyp() {
        return super.getTyp();
    }

    @Override
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    public AbrechnungsStatus getStatus() {
        return super.getStatus();
    }

    @Override
    @Basic
    @Column(name = "BEZEICHNUNG")
    public String getBezeichnung() {
        return super.getBezeichnung();
    }

    @Override
    @Basic
    @Column(name = "NUMMER")
    public int getNummer() {
        ;
        return super.getNummer();
    }

    @Override
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RUN_STATUS")
    public RunStatus getRunStatus() {
        return super.getRunStatus();
    }

    @Override
    @Basic
    @Column(name = "ANGELEGT")
    public Date getAngelegt() {
        return super.getAngelegt();
    }

    /*
     * @Override // @Basic // @Column(name = "ZEITRAUM") public Zeitraum
     * getZeitraum() {; return super.getZeitraum(); }
     */

    @Override
    @ManyToOne
    @JoinColumn(name = "MandantId")
    public Mandant getMandant() {
        return super.getMandant();
    };

    @Override
    @OneToMany(mappedBy = "abrechnung")
    public List<Buchung> getBuchung() {
        return super.getBuchung();
    };

}
