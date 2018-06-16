package boundingContext.abrechnung;

import java.util.ArrayList;
import java.util.Objects;

import javax.money.MonetaryAmount;

import mathe.bündel.Bündel;
import mathe.bündel.BündelMap;
import mathe.paare.Paar;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAufträge;
import ddd.Value;

public class TeilAbrechnungsListe<ID, KEY, GEBÜHRENDEFINITION, DATENREPOSITORY>
        extends
        ArrayList<TeilAbrechnung<ID, KEY, GEBÜHRENDEFINITION, DATENREPOSITORY>>
        implements Value {

    MehrereAbrechnungsSchritte<Paar<Beschreibung, KEY>> gesamterTeil;

    public BuchungsAufträge<ID, KEY> abrechnen(DATENREPOSITORY repo,
            Abrechnung<ID> abrechnung) {
        Objects.requireNonNull(repo);
        Objects.requireNonNull(abrechnung);

        BündelMap<String, ID> beteiligte = new BündelMap<>();
        beteiligte.put("Abrechnung", abrechnung.getId());

        BuchungsAufträge<ID, KEY> aufträge = new BuchungsAufträge(beteiligte);
        for (TeilAbrechnung<ID, KEY, GEBÜHRENDEFINITION, DATENREPOSITORY> teilAbrechnung : this) {
            aufträge.add(teilAbrechnung.abrechnen(repo));
        }
        return aufträge;
    }

    public AbrechnungsBeträge<Paar<Beschreibung, KEY>> zuEinerMapVereinigen(
            BuchungsAufträge<ID, KEY> teilAufträge) {
        AbrechnungsBeträge<Paar<Beschreibung, KEY>> beträge = new AbrechnungsBeträge<>();
        teilAufträge.inEinPaarBündelFüllen(beträge);
        return beträge;
    }

    public Bündel<Paar<Beschreibung, KEY>, MonetaryAmount> gesamtenTeilAbrechnen(
            BuchungsAufträge<ID, KEY> teilAufträge) {
        AbrechnungsBeträge<Paar<Beschreibung, KEY>> vereinigt = zuEinerMapVereinigen(teilAufträge);
        return gesamterTeil.apply(vereinigt);
    }

}
