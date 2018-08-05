package org.nill.abrechnung.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
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
import javax.validation.constraints.NotNull;

import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.aufzählungen.AbrechnungsTyp;
import org.nill.abrechnung.aufzählungen.SachKontoProvider;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.nill.zahlungen.values.MonatJahr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

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

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mandant", fetch = FetchType.LAZY)
    private Set<Abrechnung> abrechnung = new HashSet<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(name = "mandant_gebuehrdefinition", joinColumns = { @JoinColumn(name = "mandantId") }, inverseJoinColumns = { @JoinColumn(name = "gebuehrDefinitionId") })
    private Set<GebuehrDefinition> gebuehrDefinitionen = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "mandant", fetch = FetchType.LAZY)
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

    public Optional<Abrechnung> getLetzteAbgerechneteAbrechnung(
            @NotNull SachKontoProvider provider, MonatJahr mj,
            AbrechnungsTyp typ) {
        AbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();

        Integer n = abrechnungRepository.getLetzteAbgerechneteAbrechnung(this,
                AbrechnungsStatus.ABGERECHNET, mj);
        if (n != null && n > 0) {
            List<Abrechnung> liste = abrechnungRepository
                    .getAbrechnung(this, n);
            return Optional.of(liste.get(0));
        }
        return Optional.empty();
    }

    public Abrechnung createNeueAbrechnung(@NotNull SachKontoProvider provider,
            MonatJahr mj, AbrechnungsTyp typ) {
        AbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();

        Integer n = abrechnungRepository.getLetzteAbrechnung(this);
        Abrechnung neu = new Abrechnung();
        neu.setNummer((n == null) ? 1 : n.intValue() + 1);
        neu.setMandant(this);
        neu.setMj(mj);
        neu.setTyp(typ);
        return abrechnungRepository.save(neu);
    }

}
