package org.nill.abrechnung.repositories;

import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.values.KontoBewegung;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BuchungRepository extends CrudRepository<Buchung, Long> {

    List<Buchung> findByText(String lastName);

    @Query("select bew from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr  ")
    public List<KontoBewegung> getBewegungen(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr);

    @Query("select bew.art, bew.kontoNr,  sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr group by bew.art, bew.kontoNr ")
    public List<?> getSumBewegungen(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr);

    @Query("select bew.kontoNr,  sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr and bew.art = 1 and buch.art = :art   group by bew.kontoNr ")
    public List<?> getSumBewegungen(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr,
            @Param("art") int art);

    @Query("select sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr and bew.art = 1 and buch.art = :art  and bew.kontoNr = :knr  group by bew.kontoNr ")
    public MonetaryAmount getSumKonto(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr,
            @Param("art") int art, @Param("knr") int kontonr);

    @Query("select sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr and bew.art = 1 ")
    public MonetaryAmount getSaldo(
            @Param("abr") org.nill.abrechnung.entities.Abrechnung abr);

}
