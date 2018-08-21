package org.nill.abrechnung.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.Umgebung;
import org.nill.abrechnung.values.ZahlungsDefinition;
import org.nill.allgemein.values.MonatJahr;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "MANDANT")
@SequenceGenerator(name = "MANDANT_SEQ", sequenceName = "MANDANT_SEQ")
public class  Mandant implements IMandant {
    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "MANDANTID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MANDANT_SEQ")
    private java.lang.Long mandantId;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "NAME")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mandant", fetch = FetchType.LAZY)
    private Set<Abrechnung> abrechnung = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "mandant_gebuehrdefinition", joinColumns = { @JoinColumn(name = "mandantId") }, inverseJoinColumns = { @JoinColumn(name = "gebuehrDefinitionId") })
    private Set<GebührDefinition> gebuehrDefinitionen = new HashSet<>();

    
    @ElementCollection(targetClass=ZahlungsDefinition.class)
    @CollectionTable(name="mandant_zahlungsDefinitionen", joinColumns=@JoinColumn(name="MANDANTID"))
    private Set<ZahlungsDefinition> zahlungsDefinitionen = new HashSet<>();
    
    @Override
    public void addAbrechnung(IAbrechnung a) {
        abrechnung.add((Abrechnung)a);
    }
    @Override
    public void addZahlungsDefinitionen(ZahlungsDefinition d) {
        zahlungsDefinitionen.add(d);
    }

    @Override
    public void addGebuehrDefinitionen(IGebührDefinition d) {
        gebuehrDefinitionen.add((GebührDefinition)d);

    }
    

    @Override
    public Optional<IAbrechnung> getLetzteAbgerechneteAbrechnung(Umgebung provider, MonatJahr mj,
            AbrechnungsTyp typ) {
                IAbrechnungRepository abrechnungRepository = provider
                        .getAbrechnungRepository();
            
                Integer n = abrechnungRepository.getLetzteAbgerechneteAbrechnung(this,
                        AbrechnungsStatus.ABGERECHNET, mj);
                if (n != null && n > 0) {
                    List<IAbrechnung> liste = abrechnungRepository
                            .getAbrechnung(this, n);
                    return Optional.of(liste.get(0));
                }
                return Optional.empty();
            }

    @Override
    public IAbrechnung createNeueAbrechnung(Umgebung provider, MonatJahr mj, AbrechnungsTyp typ) {
        IAbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
    
        Integer n = abrechnungRepository.getLetzteAbrechnung(this);
        Abrechnung neu = new Abrechnung();
        neu.setNummer((n == null) ? 1 : n.intValue() + 1);
        neu.setIMandant(this);
        neu.setMj(mj);
        neu.setTyp(typ);
        return abrechnungRepository.saveIAbrechnung(neu);
    }
   


}
