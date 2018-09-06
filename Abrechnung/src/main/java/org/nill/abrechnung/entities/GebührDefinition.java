package org.nill.abrechnung.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.nill.abrechnung.interfaces.IGebührDefinition;
import org.nill.abrechnung.interfaces.IMandant;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "GEBUEHRDEFINITION")
@SequenceGenerator(name = "GEBUEHRDEFINITION_SEQ", sequenceName = "GEBUEHRDEFINITION_SEQ")
public class GebührDefinition implements IGebührDefinition {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Column(name = "GEBUEHRDEFINITIONID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "GEBUEHRDEFINITION_SEQ")
    private long gebuehrDefinitionId;

    @Basic
    @Column(name = "PARAMETER")
    private double parameter;

    @Basic
    @Column(name = "ART")
    private int art;

    @Basic
    @Column(name = "KONTONR")
    private int kontoNr;

    @Basic
    @Column(name = "DATENART")
    private int datenArt;

    @Basic
    @Column(name = "GEBUEHRART")
    private int gebührArt;

    @Basic
    @Column(name = "BUCHUNGSART")
    private int buchungsArt;

    @Basic
    @Column(name = "NAME")
    private String name;

    @Basic
    @Column(name = "BESCHREIBUNG")
    private String beschreibung;

    @Basic
    @Column(name = "MWSTSATZ")
    private double mwstSatz;

    @Basic
    @Column(name = "MWSTKONTO")
    private int mwstKonto;

    @Basic
    @Column(name = "BUCHUNGSTEXT")
    private String buchungstext;

    @ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST,
            CascadeType.MERGE }, mappedBy = "gebuehrDefinitionen")
    private Set<Mandant> mandanten = new HashSet<>();

    @Override
    public void addMandant(IMandant mandant) {
        this.mandanten.add((Mandant) mandant);
    }

}
