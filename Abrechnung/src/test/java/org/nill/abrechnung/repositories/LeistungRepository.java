package org.nill.abrechnung.repositories;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Leistung;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IMandant;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LeistungRepository extends CrudRepository<Leistung, Long>, ILeistungRepository {
    @Override
    @Query("select sum(leist.betrag) from org.nill.abrechnung.entities.Leistung leist where leist.abrechnung = :abr and leist.art = :art")
    public MonetaryAmount getBetrag(
            @Param("abr") IAbrechnung abr,
            @Param("art") int art);

    @Override
    @Transactional
    @Modifying( clearAutomatically = false)
    @Query("update Leistung leist set leist.abrechnung = :abr  where leist.abrechnung is null and  leist.mandant = :mand and leist.art = :art")
    public Integer markData(
            @Param("abr") IAbrechnung abr,
            @Param("mand") IMandant mand,
            @Param("art") int art);

}
