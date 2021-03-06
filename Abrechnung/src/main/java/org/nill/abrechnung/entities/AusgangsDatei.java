package org.nill.abrechnung.entities;

import java.util.Date;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.allgemein.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "AUSGANGSDATEI")
@SequenceGenerator(name = "AUSGANGSDATEI_SEQ", sequenceName = "AUSGANGSDATEI_SEQ")
public class AusgangsDatei implements IAusgangsDatei {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "AUSGANGSDATEIID")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUSGANGSDATEI_SEQ")
    private long ausgangsDateiId;

    @Basic
    private String filename;

    @Basic
    private int fileArt;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "art", column = @Column(name = "protokoll_art")),
            @AttributeOverride(name = "id", column = @Column(name = "protokoll_id")) })
    TypeReference protokoll;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "art", column = @Column(name = "quelle_art")),
            @AttributeOverride(name = "id", column = @Column(name = "quelle_id")) })
    TypeReference quelle;

    @Basic
    private Date angelegt;

    @Basic
    private Date gesendet;

}
