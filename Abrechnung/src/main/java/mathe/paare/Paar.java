package mathe.paare;

import java.util.Objects;

public class Paar<L, R> {
    private R r;
    private L l;

    public Paar(R r, L l) {
        super();
        this.r = Objects.requireNonNull(r);
        this.l = Objects.requireNonNull(l);
    }

    public R getR() {
        return r;
    }

    public L getL() {
        return l;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((l == null) ? 0 : l.hashCode());
        result = prime * result + ((r == null) ? 0 : r.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Paar other = (Paar) obj;
        if (l == null) {
            if (other.l != null)
                return false;
        } else if (!l.equals(other.l))
            return false;
        if (r == null) {
            if (other.r != null)
                return false;
        } else if (!r.equals(other.r))
            return false;
        return true;
    }

}
