package betrag;

import java.util.Locale;
import java.util.Objects;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.MonetaryAmountFactory;
import javax.money.MonetaryAmountFactoryQueryBuilder;
import javax.money.MonetaryRounding;
import javax.money.format.MonetaryAmountFormat;
import javax.money.format.MonetaryFormats;

import mathe.gruppen.Gruppe;
import ddd.Value;

/**
 * 
 * @author Thomas Nill
 * 
 *         Geldbeträge
 *
 */

public class Geld implements Gruppe<MonetaryAmount>, Value {
    private static CurrencyUnit euro;
    private static MonetaryAmountFactory<?> factory;
    private static MonetaryRounding round;
    private static MonetaryAmountFormat germanFormat;
    private static Geld gruppe;

    private Geld() {
        super();
    }

    static {
        gruppe = new Geld();
        euro = Monetary.getCurrency("EUR");
        round = Monetary.getRounding(euro);
        factory = Monetary.getAmountFactory(MonetaryAmountFactoryQueryBuilder
                .of().setFixedScale(true).setPrecision(10).setMaxScale(2)
                .build());
        germanFormat = MonetaryFormats.getAmountFormat(Locale.GERMANY);
    }

    public static Geld getGruppe() {
        return gruppe;
    }

    public static String format(MonetaryAmount amount) {
        Objects.requireNonNull(amount);
        return germanFormat.format(amount);
    }

    public static MonetaryAmount round(MonetaryAmount amount) {
        Objects.requireNonNull(amount);
        return round.apply(amount);
    }

    public static MonetaryAmount createAmount(long l) {

        return factory.setCurrency(euro.getCurrencyCode()).setNumber(l)
                .create();
    }

    public static MonetaryAmount createAmount(Number l) {
        Objects.requireNonNull(l);
        return factory.setCurrency(euro.getCurrencyCode()).setNumber(l)
                .create();
    }

    public static MonetaryAmount createAmount(double l) {
        return round.apply(factory.setCurrency(euro.getCurrencyCode())
                .setNumber(l).create());
    }

    public static MonetaryAmount percentAmount(long percent, double l) {
        return round.apply(factory.setCurrency(euro.getCurrencyCode())
                .setNumber(((percent) / 100.0) * l).create());
    }

    public static MonetaryAmount percentAmount(MonetaryAmount g, double percent) {
        Objects.requireNonNull(g);

        return round.apply(g.multiply(percent));
    }

    public static boolean absolutGrößer(MonetaryAmount a, MonetaryAmount b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.abs().isGreaterThan(b.abs());
    }

    public static MonetaryAmount getNull() {
        return createAmount(0);
    }

    @Override
    public MonetaryAmount unit() {
        return createAmount(0);
    }

    @Override
    public boolean isUnit(MonetaryAmount x) {
        Objects.requireNonNull(x);
        return x.isZero();
    }

    @Override
    public MonetaryAmount add(MonetaryAmount a, MonetaryAmount b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);
        return a.add(b);
    }

    @Override
    public MonetaryAmount negate(MonetaryAmount x) {
        return Objects.requireNonNull(x).negate();
    }

    @Override
    public boolean isElement(MonetaryAmount x) {
        Objects.requireNonNull(x);
        return true;
    }

}
