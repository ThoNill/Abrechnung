package org.nill.abrechnung.entities;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.abrechnung.interfaces.IParameter;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.MonatJahrAdapter;
import org.nill.allgemein.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PARAMETER")
@SequenceGenerator(name = "PARAMETER_SEQ", sequenceName = "PARAMETER_SEQ")
public class Parameter implements IParameter {

    @EqualsAndHashCode.Include
    @ToString.Include
    @Basic
    @Column(name = "PARAMETER")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PARAMETER_SEQ")
    private java.lang.Long ParameterId;

    @Enumerated(EnumType.ORDINAL)
    @Column(name = "KEY")
    private ParameterKey key;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "art", column = @Column(name = "ref_art")),
            @AttributeOverride(name = "id", column = @Column(name = "ref_id")) })
    TypeReference ref;

    @Basic
    @Column(name = "MJ")
    @Convert(converter = MonatJahrAdapter.class)
    private MonatJahr mj;

    @Basic
    private String wert;

 
}
