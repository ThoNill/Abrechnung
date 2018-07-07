package boundingContext.abrechnung.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundingContext.abrechnung.entities.AusgangsDatei;
import boundingContext.abrechnung.entities.‹berweisung;
import boundingContext.zahlungen.IBAN;

public interface ‹berweisungRepository extends CrudRepository<‹berweisung, Long> {

    @Query("select  distinct u.von.iban from boundingContext.abrechnung.entities.‹berweisung u where u.ausgangsDatei is null ")
    public List<IBAN> getOffeneIBAN();
    
    @Query("select u from boundingContext.abrechnung.entities.‹berweisung u where u.ausgangsDatei is null and u.von.iban = :iban")
    public List<‹berweisung> getOffene‹berweisungen(@Param("iban") IBAN iban);
    
    @Query("select u from boundingContext.abrechnung.entities.‹berweisung u where u.ausgangsDatei = :datei ")
    public List<‹berweisung> get‹berweisungen(@Param("datei") AusgangsDatei datei);
 
    
 //   @Query("select count(*), sum(u.betrag), max(u.von.iban) from boundingContext.abrechnung.entities.‹berweisung u where u.ausgangsDatei = :datei ")
 //   public Object[] getKopfDaten(@Param("datei") AusgangsDatei datei);
 
}
