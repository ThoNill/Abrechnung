package boundingContext.abrechnung.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "MANDANT")
@SequenceGenerator(name = "MANDANT_SEQ", sequenceName = "MANDANT_SEQ")
public class Mandant { 
    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "MANDANTID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANDANT_SEQ")
    private java.lang.Long MandantId;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "NAME")
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "mandant")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<Abrechnung> abrechnung = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "mandant_gebuehrdefinition", joinColumns = { @JoinColumn(name = "mandantId") }, inverseJoinColumns = { @JoinColumn(name = "gebuehrDefinitionId") })
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<GebuehrDefinition> gebuehrDefinitionen = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "mandant")
    @LazyCollection(LazyCollectionOption.FALSE)
    private Set<ZahlungsDefinition> zahlungsDefinitionen = new HashSet<>();

    public void addAbrechnung(Abrechnung a) {
        abrechnung.add(a);
    }

    public void addZahlungsDefinitionen(ZahlungsDefinition d) {
        zahlungsDefinitionen.add(d);
    }

    public void addGebuehrDefinitionen(GebuehrDefinition d) {
        gebuehrDefinitionen.add(d);

    }

}
