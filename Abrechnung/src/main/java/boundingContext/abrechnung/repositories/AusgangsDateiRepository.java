package boundingContext.abrechnung.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundingContext.abrechnung.entities.AusgangsDatei;

public interface AusgangsDateiRepository extends
        CrudRepository<AusgangsDatei, Long> {

    @Query("select a from boundingContext.abrechnung.entities.AusgangsDatei a where a.gesendet is null and a.fileArt = :art ")
    public List<AusgangsDatei> getNichVersendeteDateien(@Param("art") int art);

}
