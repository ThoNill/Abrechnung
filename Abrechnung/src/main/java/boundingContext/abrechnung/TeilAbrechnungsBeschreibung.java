package boundingContext.abrechnung;

import java.util.Objects;

import boundingContext.buchhaltung.eingang.Beschreibung;

public class TeilAbrechnungsBeschreibung<KEY, GEBÜHRDEFINITION, DATENREPOSITORY>
        extends Beschreibung {
    private AbrechnungsSchrittFabrik<KEY, GEBÜHRDEFINITION> abrechnungsSchrittFabrik;
    private BeträgeEinspeisen<KEY, DATENREPOSITORY> datenEinspeiser;

    public TeilAbrechnungsBeschreibung(
            int art,
            String text,
            AbrechnungsSchrittFabrik<KEY, GEBÜHRDEFINITION> abrechnungsSchrittFabrik,
            BeträgeEinspeisen<KEY, DATENREPOSITORY> datenEinspeiser) {
        super(art, text);
        this.abrechnungsSchrittFabrik = Objects
                .requireNonNull(abrechnungsSchrittFabrik);
        this.datenEinspeiser = Objects.requireNonNull(datenEinspeiser);
    }

    public MehrereAbrechnungsSchritte<KEY> createAbrechnungsSchritte(
            GEBÜHRDEFINITION gebührDefinition) {
        return abrechnungsSchrittFabrik.apply(gebührDefinition);
    }

    public BeträgeEinspeisen<KEY, DATENREPOSITORY> getDatenEinspeiser() {
        return datenEinspeiser;
    }

}
