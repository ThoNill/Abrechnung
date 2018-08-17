package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.AusgangsDatei;
import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.IAusgangsDateiRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AusgangsDateiRepository extends
        CrudRepository<AusgangsDatei, Long>, IAusgangsDateiRepository {
    
    @Query("select a from org.nill.abrechnung.entities.AusgangsDatei a where a.gesendet is null and a.fileArt = :art ")
    public List<IAusgangsDatei> getNichVersendeteDateien(@Param("art") int art);

    public default IAusgangsDatei save(IAusgangsDatei d) {
        return this.save((AusgangsDatei)d);
    }


}
