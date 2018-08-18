package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.Überweisung;
import org.nill.abrechnung.interfaces.IAusgangsDatei;
import org.nill.abrechnung.interfaces.IÜberweisung;
import org.nill.abrechnung.interfaces.IÜberweisungRepository;
import org.nill.zahlungen.values.IBAN;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ÜberweisungRepository extends
        CrudRepository<Überweisung, Long>, IÜberweisungRepository {

    @Override
    @Query("select  distinct u.von.iban from org.nill.abrechnung.entities.Überweisung u where u.ausgangsDatei is null ")
    public List<IBAN> getOffeneIBAN();

    @Override
    @Query("select u from org.nill.abrechnung.entities.Überweisung u where u.ausgangsDatei is null and u.von.iban = :iban")
    public List<IÜberweisung> getOffeneÜberweisungen(@Param("iban") IBAN iban);

    @Override
    @Query("select u from org.nill.abrechnung.entities.Überweisung u where u.ausgangsDatei = :datei ")
    public List<IÜberweisung> getÜberweisungen(
            @Param("datei") IAusgangsDatei datei);
    
    @Override
    default IÜberweisung save(IÜberweisung überweisung) {
        return this.save((Überweisung)überweisung);
    }

}
