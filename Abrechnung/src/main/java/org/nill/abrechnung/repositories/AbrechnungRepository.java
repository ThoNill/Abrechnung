package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.aufzählungen.AbrechnungsStatus;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.allgemein.values.MonatJahr;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AbrechnungRepository extends CrudRepository<Abrechnung, Long>, IAbrechnungRepository {

    @Override
    @Query("select abr from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.nummer = :n")
    public List<IAbrechnung> getAbrechnung(
            @Param("mand") IMandant mand,
            @Param("n") int n);

    @Override
    @Query("select max(abr.nummer) from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.status = :s and abr.mj =:mj ")
    public Integer getLetzteAbgerechneteAbrechnung(
            @Param("mand") IMandant mand,
            @Param("s") AbrechnungsStatus status, @Param("mj") MonatJahr mj);

    @Override
    @Query("select max(abr.nummer) from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand  and abr.mj =:mj ")
    public Integer getLetzteAbrechnung(
            @Param("mand") IMandant mand,
            @Param("mj") MonatJahr mj);

    @Override
    @Query("select max(abr.nummer) from org.nill.abrechnung.entities.Abrechnung abr where abr.mandant = :mand ")
    public Integer getLetzteAbrechnung(
            @Param("mand") IMandant mand);

     @Override
    public default IAbrechnung saveIAbrechnung(IAbrechnung abrechnung) {
            return save((Abrechnung)abrechnung);
    }

}
