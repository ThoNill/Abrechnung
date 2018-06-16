package app.repositories;

import javax.money.MonetaryAmount;
import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import app.entities.Leistung;

public interface LeistungRepository extends CrudRepository<Leistung, Long> {

    @Query("select sum(leist.betrag) from app.entities.Leistung leist where leist.abrechnung = :abr and leist.art = :art")
    public MonetaryAmount getBetrag(@Param("abr") app.entities.Abrechnung abr,
            @Param("art") int art);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update Leistung leist set leist.abrechnung = :abr  where leist.abrechnung is null and  leist.mandant = :mand and leist.art = :art")
    public Integer markData(@Param("abr") app.entities.Abrechnung abr,
            @Param("mand") app.entities.Mandant mand, @Param("art") int art);

}
