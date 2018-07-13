package boundingContext.abrechnung.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import boundingContext.abrechnung.aufzählungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.RunStatus;
import boundingContext.abrechnung.aufzählungen.Zeitraum;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)

@Entity
@Table(name = "ABRECHNUNG")
@SequenceGenerator(name = "ABRECHNUNG_SEQ", sequenceName = "ABRECHNUNG_SEQ")
public class Abrechnung  {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "ABRECHNUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ABRECHNUNG_SEQ")
    private java.lang.Long AbrechnungId;

    @EqualsAndHashCode.Include
    @ToString.Include
 
    @Basic
    @Column(name = "MONAT")
    private int monat;
 
    @EqualsAndHashCode.Include
    @ToString.Include
    
    @Basic
    @Column(name = "JAHR")
    private int jahr;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "STATUS")
    private AbrechnungsStatus status;

    @ManyToOne
    @JoinColumn(name = "MandantId")
    private Mandant mandant;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "TYP")
    private AbrechnungsTyp typ = AbrechnungsTyp.INITIAL;
    
    @Basic
    @Column(name = "BEZEICHNUNG")
    private String bezeichnung;
 
    @EqualsAndHashCode.Include
    @ToString.Include
 
    @Basic
    @Column(name = "NUMMER")
    private int nummer;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "RUN_STATUS")
    private RunStatus runStatus = RunStatus.CREATED;

    @Basic
    @Column(name = "ANGELEGT")
    private Date angelegt = new Date();
    
    // private Zeitraum zeitraum;

    @OneToMany(mappedBy = "abrechnung")
    private Set<Buchung> buchung = new HashSet<>();
    

    public void addBuchung(Buchung buchung) {
        this.buchung.add(buchung);
    };

    public void removeBuchung(Buchung buchung) {
        this.buchung.remove(buchung);
    };


}
