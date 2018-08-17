package org.nill.abrechnung.repositories;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IMandant;

public interface ILeistungRepository {

    public MonetaryAmount getBetrag(IAbrechnung abr, int art);

    public Integer markData(IAbrechnung abr, IMandant mand, int art);

}