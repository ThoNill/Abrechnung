package boundingContext.abrechnung;

import java.util.Objects;

import boundingContext.buchhaltung.eingang.Beschreibung;

public class TeilAbrechnungsBeschreibung<KEY, GEB�HRDEFINITION, DATENREPOSITORY>
        extends Beschreibung {
    private AbrechnungsSchrittFabrik<KEY, GEB�HRDEFINITION> abrechnungsSchrittFabrik;
    private Betr�geEinspeisen<KEY, DATENREPOSITORY> datenEinspeiser;

    public TeilAbrechnungsBeschreibung(
            int art,
            String text,
            AbrechnungsSchrittFabrik<KEY, GEB�HRDEFINITION> abrechnungsSchrittFabrik,
            Betr�geEinspeisen<KEY, DATENREPOSITORY> datenEinspeiser) {
        super(art, text);
        this.abrechnungsSchrittFabrik = Objects
                .requireNonNull(abrechnungsSchrittFabrik);
        this.datenEinspeiser = Objects.requireNonNull(datenEinspeiser);
    }

    public MehrereAbrechnungsSchritte<KEY> createAbrechnungsSchritte(
            GEB�HRDEFINITION geb�hrDefinition) {
        return abrechnungsSchrittFabrik.apply(geb�hrDefinition);
    }

    public Betr�geEinspeisen<KEY, DATENREPOSITORY> getDatenEinspeiser() {
        return datenEinspeiser;
    }

}
