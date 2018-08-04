package boundingContext.abrechnung.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundingContext.abrechnung.aufzählungen.AbrechnungsStatus;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.zahlungen.values.MonatJahr;

public interface AbrechnungRepository extends CrudRepository<Abrechnung, Long> {

    @Query("select abr from boundingContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.nummer = :n")
    public List<Abrechnung> getAbrechnung(
            @Param("mand") boundingContext.abrechnung.entities.Mandant mand,
            @Param("n") int n);

    @Query("select max(abr.nummer) from boundingContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.status = :s and abr.mj =:mj ")
    public Integer getLetzteAbgerechneteAbrechnung(
            @Param("mand") boundingContext.abrechnung.entities.Mandant mand,
            @Param("s") AbrechnungsStatus status, @Param("mj") MonatJahr mj);

    @Query("select max(abr.nummer) from boundingContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand  and abr.mj =:mj ")
    public Integer getLetzteAbrechnung(
            @Param("mand") boundingContext.abrechnung.entities.Mandant mand,
            @Param("mj") MonatJahr mj);

    @Query("select max(abr.nummer) from boundingContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand ")
    public Integer getLetzteAbrechnung(
            @Param("mand") boundingContext.abrechnung.entities.Mandant mand);

}
