package tests;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.abrechnung.AbrechnungsDatenRepository;
import boundingContext.abrechnung.entities.Abrechnung;

public class TestDatenRepository implements AbrechnungsDatenRepository<Long> {

    @Override
    public void markiereDaten(Abrechnung<Long> abrechnung) {
    }

    public MonetaryAmount getBetragA() {
        return Geld.createAmount(20000.0d);
    }

    public MonetaryAmount getBetragB() {
        return Geld.createAmount(400.0d);
    }

}
