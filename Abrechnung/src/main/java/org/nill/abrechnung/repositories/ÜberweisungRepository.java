package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.�berweisung;
import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.I�berweisung;
import org.nill.abrechnung.interfaces.I�berweisungRepository;
import org.nill.zahlungen.values.IBAN;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface �berweisungRepository extends
        CrudRepository<�berweisung, Long>, I�berweisungRepository {

    @Override
    @Query("select  distinct u.von.iban from org.nill.abrechnung.entities.�berweisung u where u.ausgangsDatei is null ")
    public List<IBAN> getOffeneIBAN();

    @Override
    @Query("select u from org.nill.abrechnung.entities.�berweisung u where u.ausgangsDatei is null and u.von.iban = :iban")
    public List<I�berweisung> getOffene�berweisungen(@Param("iban") IBAN iban);

    @Override
    @Query("select u from org.nill.abrechnung.entities.�berweisung u where u.ausgangsDatei = :datei ")
    public List<I�berweisung> get�berweisungen(
            @Param("datei") IAusgangsDatei datei);
    
    @Override
    default I�berweisung save(I�berweisung �berweisung) {
        return this.save((�berweisung)�berweisung);
    }

}
