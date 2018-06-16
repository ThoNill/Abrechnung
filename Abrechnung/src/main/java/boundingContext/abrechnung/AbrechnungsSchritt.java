package boundingContext.abrechnung;

import java.util.function.Function;

public interface AbrechnungsSchritt<KEY> extends
        Function<AbrechnungsBeträge<KEY>, AbrechnungsBeträge<KEY>> {

}
