package org.nill.abrechnung.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
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

import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.values.KontoBewegung;
import org.nill.allgemein.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "BUCHUNG")
@SequenceGenerator(name = "BUCHUNG_SEQ", sequenceName = "BUCHUNG_SEQ")
public class Buchung implements IBuchung {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "BUCHUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUCHUNG_SEQ")
    private java.lang.Long buchungId;

    @Basic
    @Column(name = "BUCHUNGSDATUM")
    private Date buchungsDatum;

    @ToString.Include
    @Basic
    @Column(name = "TEXT")
    private String text;

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "ART")
    private int art;

    @ElementCollection
    @CollectionTable(name="Buchung_KontoBewegung", joinColumns=@JoinColumn(name="BUCHUNGID"))
    private Set<KontoBewegung> bewegungen = new HashSet<>();


    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AbrechnungId")
    private Abrechnung abrechnung;

    @ElementCollection
    @CollectionTable(name="Buchung_TypeReference", joinColumns=@JoinColumn(name="BUCHUNGID"))
    private Set<TypeReference> bezüge = new HashSet<>();

    
    @Override
    public void addBewegungen(KontoBewegung bewegungen) {
        this.bewegungen.add(bewegungen);
    }

    @Override
    public void removeBewegungen(KontoBewegung bewegungen) {
        this.bewegungen.remove(bewegungen);
    }


    @Override
    public void addBezug(TypeReference bezug) {
        this.bezüge.add(bezug);
    }

    @Override
    public void removeBezug(TypeReference bezug) {
        this.bezüge.remove(bezug);
    }

    @Override
    public void setIAbrechnung(IAbrechnung abrechnung2) {
        setAbrechnung((Abrechnung)abrechnung2);
    }


}
