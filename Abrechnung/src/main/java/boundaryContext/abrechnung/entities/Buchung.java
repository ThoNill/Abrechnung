package boundaryContext.abrechnung.entities;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "BUCHUNG")
@SequenceGenerator(name = "BUCHUNG_SEQ", sequenceName = "BUCHUNG_SEQ")
public class Buchung extends BuchungUser {

    public Buchung() {
        super();
    }

    @Override
    @Basic
    @Column(name = "BUCHUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUCHUNG_SEQ")
    public java.lang.Long getBuchungId() {
        return super.getBuchungId();
    };

    @Override
    @Basic
    @Column(name = "TEXT")
    public String getText() {
        return super.getText();
    };

    @Override
    @Basic
    @Column(name = "ART")
    public int getArt() {
        return super.getArt();
    };

    @Override
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "buchung")
    public List<KontoBewegung> getBewegungen() {
        return super.getBewegungen();
    };

    @Override
    @ManyToOne()
    @JoinColumn(name = "AbrechnungId")
    public Abrechnung getAbrechnung() {
        return super.getAbrechnung();
    };

}
