package boundingContext.abrechnung;

import java.util.function.Function;

public interface AbrechnungsSchritt<KEY> extends
        Function<AbrechnungsBetr�ge<KEY>, AbrechnungsBetr�ge<KEY>> {

}
