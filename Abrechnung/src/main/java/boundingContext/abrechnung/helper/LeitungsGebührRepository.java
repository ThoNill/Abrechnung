package boundingContext.abrechnung.helper;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.aufzählungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.daten.GebührRepository;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;

public class LeitungsGebührRepository implements GebührRepository<SachKonto> {

    private LeistungRepository leistungRepository;

    private SachKonto sachKonto;
    private int art;

    public LeitungsGebührRepository(SachKonto sachKonto, int art) {
        super();
        this.sachKonto = sachKonto;
        this.art = art;
    }

    @Override
    public void markieren(Abrechnung abrechnung) {
        leistungRepository.markData(abrechnung, abrechnung.getMandant(), art);
    }

    @Override
    public BetragsBündel<SachKonto> getBeträge(Abrechnung abrechnung) {
        MonetaryAmount betrag = getGebührenBasis(abrechnung);
        BetragsBündelMap<SachKonto> bündel = new BetragsBündelMap<>();
        bündel.put(sachKonto, betrag);
        return bündel;
    }

    @Override
    public MonetaryAmount getGebührenBasis(Abrechnung abrechnung) {
        return leistungRepository.getBetrag(abrechnung, art);
    }

    public void setLeistungsRepository(LeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

}
