package boundingContext.abrechnung;

import java.util.function.Function;

public interface Betr�geEinspeisen<KEY, DATENREPOSITORY> extends
        Function<DATENREPOSITORY, AbrechnungsBetr�ge<KEY>> {

}
