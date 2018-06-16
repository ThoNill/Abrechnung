package boundingContext.buchhaltung;

import java.util.Objects;

import javax.money.MonetaryAmount;

import betrag.Geld;
import boundingContext.gemeinsam.BetragsB�ndelMap;

public class SaldoBerechnen<KEY> {
    KEY zahlungAusl�sen;
    KEY �berZahlung;

    public SaldoBerechnen(KEY zahlungAusl�sen, KEY �berZahlung) {
        super();
        this.zahlungAusl�sen = Objects.requireNonNull(zahlungAusl�sen);
        this.�berZahlung = Objects.requireNonNull(�berZahlung);
    }

    public BetragsB�ndelMap<KEY> apply(BetragsB�ndelMap<KEY> t) {
        BetragsB�ndelMap<KEY> c = t.copy();
        MonetaryAmount saldo = c.getSumme();
        if (saldo.isGreaterThan(Geld.getNull())) {
            c.put(zahlungAusl�sen, saldo.negate());
        } else {
            c.put(�berZahlung, saldo.negate());
        }
        return c;
    }

}
