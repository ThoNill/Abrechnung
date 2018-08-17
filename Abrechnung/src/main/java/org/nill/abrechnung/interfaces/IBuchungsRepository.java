package org.nill.abrechnung.interfaces;

import java.util.List;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.entities.Buchung;
import org.nill.abrechnung.values.KontoBewegung;

public interface IBuchungsRepository {

    public List<Buchung> findByText(String lastName);

    public List<KontoBewegung> getBewegungen(IAbrechnung abr);

    public List<?> getSumBewegungen(IAbrechnung abr);

    public List<?> getSumBewegungen(IAbrechnung abr, int art);

    public MonetaryAmount getSumKonto(IAbrechnung abr, int art, int kontonr);

    public MonetaryAmount getSaldo(IAbrechnung abr);

    public IBuchung save(IBuchung buchung);

}