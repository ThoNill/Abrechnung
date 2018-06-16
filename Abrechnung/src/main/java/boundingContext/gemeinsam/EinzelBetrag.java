package boundingContext.gemeinsam;

import java.util.Objects;
import java.util.function.Function;

import javax.money.MonetaryAmount;

public interface EinzelBetrag<REPO> extends Function<REPO, MonetaryAmount> {

    default MonetaryAmount getBetrag(REPO repo) {
        return apply(Objects.requireNonNull(repo));
    };
}
