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

import org.nill.abrechnung.values.KontoBewegung;
import org.nill.allgemein.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "BUCHUNG")
@SequenceGenerator(name = "BUCHUNG_SEQ", sequenceName = "BUCHUNG_SEQ")
public class Buchung {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "BUCHUNGID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BUCHUNG_SEQ")
    private java.lang.Long BuchungId;

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

    public void addBewegungen(KontoBewegung bewegungen) {
        this.bewegungen.add(bewegungen);
    };

    public void removeBewegungen(KontoBewegung bewegungen) {
        this.bewegungen.remove(bewegungen);
    };

    @ElementCollection
    @CollectionTable(name="Buchung_TypeReference", joinColumns=@JoinColumn(name="BUCHUNGID"))
    private Set<TypeReference> bez�ge = new HashSet<>();

    public void addBezug(TypeReference bezug) {
        this.bez�ge.add(bezug);
    };

    public void removeBezug(TypeReference bezug) {
        this.bez�ge.remove(bezug);
    };

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "AbrechnungId")
    private Abrechnung abrechnung;

}
