package boundingContext.abrechnung.entities;

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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "LEISTUNG")
@SequenceGenerator(name = "LEISTUNG_SEQ", sequenceName = "LEISTUNG_SEQ")
public class Leistung {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "LEISTUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "LEISTUNG_SEQ")
    private long leistungId;

    @Basic
    @Column(name = "BETRAG")
    @Convert(converter = betrag.GeldKonverter.class)
    private MonetaryAmount betrag;

    @Basic
    @Column(name = "ART")
    private int art;

    @ManyToOne()
    @JoinColumn(name = "AbrechnungId")
    private Abrechnung abrechnung;

    @ManyToOne()
    @JoinColumn(name = "MandantId")
    private Mandant mandant;

}
