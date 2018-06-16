package boundingContext.abrechnung;

import java.util.Objects;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.geb�hren.Geb�hr;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.EinzelBetrag;
import ddd.Value;

public class Geb�hrBerechnen<KEY> implements AbrechnungsSchritt<KEY>, Value {
    private KEY geb�hrPosition;
    private EinzelBetrag<BetragsB�ndel<KEY>> betrag;
    private Geb�hr geb�hr;

    public Geb�hrBerechnen(KEY geb�hrPosition, Geb�hr geb�hr,
            EinzelBetrag<BetragsB�ndel<KEY>> betrag) {
        this.geb�hr = Objects.requireNonNull(geb�hr);
        this.geb�hrPosition = Objects.requireNonNull(geb�hrPosition);
        this.betrag = Objects.requireNonNull(betrag);
    }

    @Override
    public AbrechnungsBetr�ge<KEY> apply(AbrechnungsBetr�ge<KEY> b�ndel) {
        Objects.requireNonNull(b�ndel);

        AbrechnungsBetr�ge<KEY> repo = new AbrechnungsBetr�ge<KEY>();
        repo.erweitern(b�ndel);
        MonetaryAmount geb�hrBetrag = geb�hr
                .getGeb�hr(betrag.getBetrag(b�ndel)).negate();
        repo.put(geb�hrPosition, geb�hrBetrag);
        return repo;
    }

}
