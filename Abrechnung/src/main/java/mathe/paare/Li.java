package mathe.paare;

import java.util.function.Predicate;

public class Li<R, L> implements Predicate<Paar<R, L>> {
    private L l;

    public Li(L l) {
        super();
        this.l = l;
    }

    @Override
    public boolean test(Paar<R, L> t) {
        return l.equals(t.getL());
    }

}
