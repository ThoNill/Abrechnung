package boundingContext.abrechnung;

import java.util.function.Function;

public interface AbrechnungsSchrittFabrik<KEY, GEB�HRENDEFINITION> extends
        Function<GEB�HRENDEFINITION, MehrereAbrechnungsSchritte<KEY>> {

}
