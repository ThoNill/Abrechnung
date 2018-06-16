package boundingContext.gemeinsam;

import java.util.Arrays;
import java.util.Objects;

import javax.money.MonetaryAmount;

public class EinzelBetragAusBündel<KEY> implements
        EinzelBetrag<BetragsBündel<KEY>> {
    private Iterable<KEY> positionen;

    public EinzelBetragAusBündel(Iterable<KEY> positionen) {
        super();
        this.positionen = Objects.requireNonNull(positionen);
    }

    public EinzelBetragAusBündel(KEY... positionen) {
        this(Arrays.asList(Objects.requireNonNull(positionen)));
    }

    @Override
    public MonetaryAmount apply(BetragsBündel<KEY> bündel) {
        return Objects.requireNonNull(bündel).getSumme(positionen);
    }

}
