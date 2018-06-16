package mathe.paare;

import java.util.function.Predicate;

public class Re<R, L> implements Predicate<Paar<R, L>> {
    private R r;

    public Re(R r) {
        super();
        this.r = r;
    }

    @Override
    public boolean test(Paar<R, L> t) {
        return r.equals(t.getR());
    }

}
