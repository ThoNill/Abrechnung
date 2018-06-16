package boundingContext.abrechnung;

import java.util.function.Function;

public interface BeträgeEinspeisen<KEY, DATENREPOSITORY> extends
        Function<DATENREPOSITORY, AbrechnungsBeträge<KEY>> {

}
