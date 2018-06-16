package boundingContext.zahlungen;

import java.util.Date;

import javax.money.MonetaryAmount;

import ddd.Entity;

public interface ZahlungsAuftrag<ID> extends Entity<ID> {

    MonetaryAmount getBetrag();

    Date getZahlungsDatum();

    BankVerbindung getBankVerbindung();

}
