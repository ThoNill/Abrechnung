package mathe.gruppen;

public interface Gruppe<ELEMENT> {

    boolean isElement(ELEMENT x);

    boolean isUnit(ELEMENT x);

    ELEMENT add(ELEMENT a, ELEMENT b);

    ELEMENT negate(ELEMENT x);

    ELEMENT unit();
}
