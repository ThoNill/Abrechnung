package boundingContext.abrechnung;

import java.util.Objects;

import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftrag;
import ddd.Value;

public class TeilAbrechnung<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY>
        implements Value {
    private Betr�geEinspeisen<KEY, DATENREPOSITORY> einspeisen;
    private MehrereAbrechnungsSchritte<KEY> schritte;
    private TeilAbrechnungsBeschreibung beschreibung;

    public TeilAbrechnung(
            GEB�HRENDEFINITION geb�hrDefinition,
            TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, DATENREPOSITORY> beschreibung) {
        super();
        this.beschreibung = Objects.requireNonNull(beschreibung);
        this.einspeisen = beschreibung.getDatenEinspeiser();
        this.schritte = beschreibung
                .createAbrechnungsSchritte(geb�hrDefinition);

    }

    public BuchungsAuftrag<KEY> abrechnen(DATENREPOSITORY repo) {
        AbrechnungsBetr�ge<KEY> betr�ge = einspeisen.apply(repo);
        AbrechnungsBetr�ge<KEY> abrechnungsBetr�ge = schritte.apply(betr�ge);
        return new BuchungsAuftrag<KEY>(beschreibung, abrechnungsBetr�ge);
    }

    public Beschreibung getBeschreibung() {
        return beschreibung;
    }

}
