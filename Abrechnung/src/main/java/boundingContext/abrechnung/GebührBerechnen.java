package boundingContext.abrechnung;

import java.util.Objects;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.gebühren.Gebühr;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.EinzelBetrag;
import ddd.Value;

public class GebührBerechnen<KEY> implements AbrechnungsSchritt<KEY>, Value {
    private KEY gebührPosition;
    private EinzelBetrag<BetragsBündel<KEY>> betrag;
    private Gebühr gebühr;

    public GebührBerechnen(KEY gebührPosition, Gebühr gebühr,
            EinzelBetrag<BetragsBündel<KEY>> betrag) {
        this.gebühr = Objects.requireNonNull(gebühr);
        this.gebührPosition = Objects.requireNonNull(gebührPosition);
        this.betrag = Objects.requireNonNull(betrag);
    }

    @Override
    public AbrechnungsBeträge<KEY> apply(AbrechnungsBeträge<KEY> bündel) {
        Objects.requireNonNull(bündel);

        AbrechnungsBeträge<KEY> repo = new AbrechnungsBeträge<KEY>();
        repo.erweitern(bündel);
        MonetaryAmount gebührBetrag = gebühr
                .getGebühr(betrag.getBetrag(bündel)).negate();
        repo.put(gebührPosition, gebührBetrag);
        return repo;
    }

}
