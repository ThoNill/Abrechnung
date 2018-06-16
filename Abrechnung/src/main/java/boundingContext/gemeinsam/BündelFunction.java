package boundingContext.gemeinsam;

import java.util.function.Function;

import mathe.bündel.Bündel;

public interface BündelFunction<KEY, ELEMENT> extends
        Function<Bündel<KEY, ELEMENT>, Bündel<KEY, ELEMENT>> {

}
