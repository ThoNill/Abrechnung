package boundingContext.abrechnung;

import java.util.ArrayList;
import java.util.Objects;

import javax.money.MonetaryAmount;

import mathe.b�ndel.B�ndel;
import mathe.b�ndel.B�ndelMap;
import mathe.paare.Paar;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.buchhaltung.eingang.Beschreibung;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;
import ddd.Value;

public class TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY>
        extends
        ArrayList<TeilAbrechnung<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY>>
        implements Value {

    MehrereAbrechnungsSchritte<Paar<Beschreibung, KEY>> gesamterTeil;

    public BuchungsAuftr�ge<ID, KEY> abrechnen(DATENREPOSITORY repo,
            Abrechnung<ID> abrechnung) {
        Objects.requireNonNull(repo);
        Objects.requireNonNull(abrechnung);

        B�ndelMap<String, ID> beteiligte = new B�ndelMap<>();
        beteiligte.put("Abrechnung", abrechnung.getId());

        BuchungsAuftr�ge<ID, KEY> auftr�ge = new BuchungsAuftr�ge(beteiligte);
        for (TeilAbrechnung<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> teilAbrechnung : this) {
            auftr�ge.add(teilAbrechnung.abrechnen(repo));
        }
        return auftr�ge;
    }

    public AbrechnungsBetr�ge<Paar<Beschreibung, KEY>> zuEinerMapVereinigen(
            BuchungsAuftr�ge<ID, KEY> teilAuftr�ge) {
        AbrechnungsBetr�ge<Paar<Beschreibung, KEY>> betr�ge = new AbrechnungsBetr�ge<>();
        teilAuftr�ge.inEinPaarB�ndelF�llen(betr�ge);
        return betr�ge;
    }

    public B�ndel<Paar<Beschreibung, KEY>, MonetaryAmount> gesamtenTeilAbrechnen(
            BuchungsAuftr�ge<ID, KEY> teilAuftr�ge) {
        AbrechnungsBetr�ge<Paar<Beschreibung, KEY>> vereinigt = zuEinerMapVereinigen(teilAuftr�ge);
        return gesamterTeil.apply(vereinigt);
    }

}
