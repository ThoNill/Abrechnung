package org.nill.abrechnung.repositories;

import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.entities.KontoBewegung;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BuchungRepository extends CrudRepository<Buchung, Long> {

    List<Buchung> findByText(String lastName);

    @Query("select bew from org.nill.abrechnung.entities.KontoBewegung bew where bew.buchung.abrechnung = :abr ")
    public List<KontoBewegung> getBewegungen(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr);

    @Query("select bew.art, bew.kontoNr,  sum(bew.betrag) from org.nill.abrechnung.entities.KontoBewegung bew where bew.buchung.abrechnung = :abr  group by bew.art, bew.kontoNr ")
    public List<?> getSumBewegungen(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr);

    @Query("select bew.kontoNr,  sum(bew.betrag) from org.nill.abrechnung.entities.KontoBewegung bew where bew.art = 1 and bew.buchung.art = :art  and bew.buchung.abrechnung = :abr  group by bew.kontoNr ")
    public List<?> getSumBewegungen(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr,
            @Param("art") int art);

    @Query("select sum(bew.betrag) from org.nill.abrechnung.entities.KontoBewegung bew where bew.art = 1 and bew.buchung.art = :art  and bew.kontoNr = :knr and bew.buchung.abrechnung = :abr  group by bew.kontoNr ")
    public MonetaryAmount getSumKonto(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr,
            @Param("art") int art, @Param("knr") int kontonr);

    @Query("select sum(bew.betrag) from org.nill.abrechnung.entities.KontoBewegung bew where bew.art = 1 and bew.buchung.abrechnung = :abr ")
    public MonetaryAmount getSaldo(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr);

}
