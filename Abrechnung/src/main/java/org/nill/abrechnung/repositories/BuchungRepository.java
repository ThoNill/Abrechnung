package org.nill.abrechnung.repositories;

import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IBuchung;
import org.nill.abrechnung.interfaces.IBuchungsRepository;
import org.nill.abrechnung.values.KontoBewegung;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BuchungRepository extends CrudRepository<Buchung, Long>,
        IBuchungsRepository {

    @Override
    List<Buchung> findByText(String lastName);

    @Override
    @Query("select bew from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr  ")
    public List<KontoBewegung> getBewegungen(@Param("abr") IAbrechnung abr);

    @Override
    @Query("select bew.art, bew.kontoNr,  sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr group by bew.art, bew.kontoNr ")
    public List<?> getSumBewegungen(@Param("abr") IAbrechnung abr);

    @Override
    @Query("select bew.kontoNr,  sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr and bew.art = 1 and buch.art = :art   group by bew.kontoNr ")
    public List<?> getSumBewegungen(@Param("abr") IAbrechnung abr,
            @Param("art") int art);

    @Override
    @Query("select sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr and bew.art = 1 and buch.art = :art  and bew.kontoNr = :knr  group by bew.kontoNr ")
    public MonetaryAmount getSumKonto(@Param("abr") IAbrechnung abr,
            @Param("art") int art, @Param("knr") int kontonr);

    @Override
    @Query("select sum(bew.betrag) from org.nill.abrechnung.entities.Buchung buch JOIN buch.bewegungen bew where buch.abrechnung = :abr and bew.art = 1 ")
    public MonetaryAmount getSaldo(@Param("abr") IAbrechnung abr);

    @Override
    default IBuchung save(IBuchung buchung) {
        return this.save((Buchung) buchung);
    }

}
