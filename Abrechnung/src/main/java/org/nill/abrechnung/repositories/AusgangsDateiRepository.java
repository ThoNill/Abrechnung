package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.AusgangsDatei;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AusgangsDateiRepository extends
        CrudRepository<AusgangsDatei, Long> {

    @Query("select a from org.nill.abrechnung.entities.AusgangsDatei a where a.gesendet is null and a.fileArt = :art ")
    public List<AusgangsDatei> getNichVersendeteDateien(@Param("art") int art);

}
