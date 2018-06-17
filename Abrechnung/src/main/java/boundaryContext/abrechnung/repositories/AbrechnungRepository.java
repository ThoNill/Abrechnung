package boundaryContext.abrechnung.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.aufzählungen.RunStatus;

public interface AbrechnungRepository extends CrudRepository<Abrechnung, Long> {

    @Query("select abr from boundaryContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.nummer = :n")
    public List<Abrechnung> getAbrechnung(
            @Param("mand") boundaryContext.abrechnung.entities.Mandant mand, @Param("n") int n);

    @Query("select max(abr.nummer) from boundaryContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand and abr.runStatus = :s and abr.monat =:m and abr.jahr = :j")
    public Integer getLetzteAbgerechneteAbrechnung(
            @Param("mand") boundaryContext.abrechnung.entities.Mandant mand,
            @Param("s") RunStatus status, @Param("m") int monat,
            @Param("j") int jahr);

    @Query("select max(abr.nummer) from boundaryContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand  and abr.monat =:m and abr.jahr = :j ")
    public Integer getLetzteAbrechnung(
            @Param("mand") boundaryContext.abrechnung.entities.Mandant mand, @Param("m") int monat,
            @Param("j") int jahr);

    @Query("select max(abr.nummer) from boundaryContext.abrechnung.entities.Abrechnung abr where abr.mandant = :mand ")
    public Integer getLetzteAbrechnung(@Param("mand") boundaryContext.abrechnung.entities.Mandant mand);

}
