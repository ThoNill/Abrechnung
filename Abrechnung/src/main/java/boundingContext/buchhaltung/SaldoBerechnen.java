package boundingContext.buchhaltung;

import java.util.Objects;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.gemeinsam.BetragsBündelMap;

public class SaldoBerechnen<KEY> {
    KEY zahlungAuslösen;
    KEY überZahlung;

    public SaldoBerechnen(KEY zahlungAuslösen, KEY überZahlung) {
        super();
        this.zahlungAuslösen = Objects.requireNonNull(zahlungAuslösen);
        this.überZahlung = Objects.requireNonNull(überZahlung);
    }

    public BetragsBündelMap<KEY> apply(BetragsBündelMap<KEY> t) {
        BetragsBündelMap<KEY> c = t.copy();
        MonetaryAmount saldo = c.getSumme();
        if (saldo.isGreaterThan(Geld.getNull())) {
            c.put(zahlungAuslösen, saldo.negate());
        } else {
            c.put(überZahlung, saldo.negate());
        }
        return c;
    }

}
