package boundingContext.abrechnung;

import java.util.Objects;

import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import ddd.Value;

public class TeilAbrechnung<ID, KEY, GEBÜHRENDEFINITION, DATENREPOSITORY>
        implements Value {
    private BeträgeEinspeisen<KEY, DATENREPOSITORY> einspeisen;
    private MehrereAbrechnungsSchritte<KEY> schritte;
    private TeilAbrechnungsBeschreibung beschreibung;

    public TeilAbrechnung(
            GEBÜHRENDEFINITION gebührDefinition,
            TeilAbrechnungsBeschreibung<KEY, GEBÜHRENDEFINITION, DATENREPOSITORY> beschreibung) {
        super();
        this.beschreibung = Objects.requireNonNull(beschreibung);
        this.einspeisen = beschreibung.getDatenEinspeiser();
        this.schritte = beschreibung
                .createAbrechnungsSchritte(gebührDefinition);

    }

    public BuchungsAuftrag<KEY> abrechnen(DATENREPOSITORY repo) {
        AbrechnungsBeträge<KEY> beträge = einspeisen.apply(repo);
        AbrechnungsBeträge<KEY> abrechnungsBeträge = schritte.apply(beträge);
        return new BuchungsAuftrag<KEY>(beschreibung, abrechnungsBeträge);
    }

    public Beschreibung getBeschreibung() {
        return beschreibung;
    }

}
