package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.allgemein.values.MonatJahr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AbrechnungRepository extends CrudRepository<Abrechnung, Long> {

    @Query("select abr from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.nummer = :n")
    public List<Abrechnung> getAbrechnung(
            @Param("mand") org.nill.abrechnung.entities.Mandant mand,
            @Param("n") int n);

    @Query("select max(abr.nummer) from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.status = :s and abr.mj =:mj ")
    public Integer getLetzteAbgerechneteAbrechnung(
            @Param("mand") org.nill.abrechnung.entities.Mandant mand,
            @Param("s") AbrechnungsStatus status, @Param("mj") MonatJahr mj);

    @Query("select max(abr.nummer) from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand  and abr.mj =:mj ")
    public Integer getLetzteAbrechnung(
            @Param("mand") org.nill.abrechnung.entities.Mandant mand,
            @Param("mj") MonatJahr mj);

    @Query("select max(abr.nummer) from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand ")
    public Integer getLetzteAbrechnung(
            @Param("mand") org.nill.abrechnung.entities.Mandant mand);

}
