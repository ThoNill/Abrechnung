package boundingContext.abrechnung.entities;

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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "KONTOBEWEGUNG")
@SequenceGenerator(name = "KONTOBEWEGUNG_SEQ", sequenceName = "KONTOBEWEGUNG_SEQ")
public class KontoBewegung {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "KONTOBEWEGUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KONTOBEWEGUNG_SEQ")
    private java.lang.Long KontoBewegungId;

    @Basic
    @Column(name = "ART")
    @EqualsAndHashCode.Include
    @ToString.Include
    private int art;
    @Basic
    @Column(name = "KONTONR")
    @EqualsAndHashCode.Include
    @ToString.Include
    private int kontoNr;

    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = betrag.GeldKonverter.class)
    private javax.money.MonetaryAmount betrag;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "BuchungId")
    private Buchung buchung;

}
