package boundingContext.abrechnung.entities;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import boundingContext.zahlungen.values.BankVerbindung;
import boundingContext.zahlungen.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)


@Entity
@Table(name = "BEZUG_ZUR_BUCHUNG")
@SequenceGenerator(name = "BEZUG_ZUR_BUCHUNG_SEQ", sequenceName = "BEZUG_ZUR_BUCHUNG_SEQ")
public class BezugZurBuchung {
    
    @EqualsAndHashCode.Include
    @ToString.Include

    @Basic
    @Column(name = "BEZUGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BEZUG_ZUR_BUCHUNG_SEQ")
    private long bezugId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "buchungsId")
    private Buchung buchung;
    
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "art", column = @Column(name = "referenz_art")),
            @AttributeOverride(name = "id", column = @Column(name = "referenz_id")) })
    TypeReference referenz;


    
}
