package boundingContext.abrechnung;

import java.util.function.Function;

public interface AbrechnungsSchrittFabrik<KEY, GEBÜHRENDEFINITION> extends
        Function<GEBÜHRENDEFINITION, MehrereAbrechnungsSchritte<KEY>> {

}
