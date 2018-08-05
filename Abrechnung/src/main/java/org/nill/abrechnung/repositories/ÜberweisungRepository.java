package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.AusgangsDatei;
import org.nill.abrechnung.entities.‹berweisung;
import org.nill.zahlungen.values.IBAN;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ‹berweisungRepository extends
        CrudRepository<‹berweisung, Long> {

    @Query("select  distinct u.von.iban from org.nill.abrechnung.entities.‹berweisung u where u.ausgangsDatei is null ")
    public List<IBAN> getOffeneIBAN();

    @Query("select u from org.nill.abrechnung.entities.‹berweisung u where u.ausgangsDatei is null and u.von.iban = :iban")
    public List<‹berweisung> getOffene‹berweisungen(@Param("iban") IBAN iban);

    @Query("select u from org.nill.abrechnung.entities.‹berweisung u where u.ausgangsDatei = :datei ")
    public List<‹berweisung> get‹berweisungen(
            @Param("datei") AusgangsDatei datei);

}
