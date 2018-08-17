package org.nill.abrechnung.interfaces;

import javax.money.MonetaryAmount;

public interface ILeistungRepository {

    public MonetaryAmount getBetrag(IAbrechnung abr, int art);

    public Integer markData(IAbrechnung abr, IMandant mand, int art);

}