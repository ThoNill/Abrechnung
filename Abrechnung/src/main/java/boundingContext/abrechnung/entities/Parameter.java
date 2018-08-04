package boundingContext.abrechnung.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.money.MonetaryAmount;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import boundingContext.abrechnung.actions.GebührenBerechnung;
import boundingContext.abrechnung.actions.SaldoAusgleichen;
import boundingContext.abrechnung.actions.SchuldenInDieAbrechnung;
import boundingContext.abrechnung.aufzählungen.AbrechnungsStatus;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.ParameterKey;
import boundingContext.abrechnung.aufzählungen.RunStatus;
import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.flow.handler.AbrechnungsKonfigurator;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import boundingContext.buchhaltung.eingang.EinBucher;
import boundingContext.zahlungen.actions.ZahlungenEntfernen;
import boundingContext.zahlungen.values.MonatJahr;
import boundingContext.zahlungen.values.MonatJahrAdapter;
import boundingContext.zahlungen.values.TypeReference;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "PARAMETER")
@SequenceGenerator(name = "PARAMETER_SEQ", sequenceName = "PARAMETER_SEQ")
public class Parameter {

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
