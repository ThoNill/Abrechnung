package boundingContext.buchhaltung;

import javax.money.MonetaryAmount;

import ddd.Entity;

public interface KontoBewegung extends Entity<Long> {
    Buchung getBuchung();

    // Konto getKonto();

    MonetaryAmount getBetrag();
}
