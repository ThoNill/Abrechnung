package boundingContext.gemeinsam;

import java.util.function.Function;

import mathe.b�ndel.B�ndel;

public interface B�ndelFunction<KEY, ELEMENT> extends
        Function<B�ndel<KEY, ELEMENT>, B�ndel<KEY, ELEMENT>> {

}
