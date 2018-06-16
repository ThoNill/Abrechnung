package tests;

import javax.money.MonetaryAmount;

import app.repositories.LeistungRepository;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.daten.GebührRepository;
import boundingContext.gemeinsam.BetragsBündel;
import boundingContext.gemeinsam.BetragsBündelMap;

public class LeitungsGebührRepository implements GebührRepository<Position> {

    private LeistungRepository leistungRepository;

    private Position position;
    private int art;

    public LeitungsGebührRepository(Position position, int art) {
        super();
        this.position = position;
        this.art = art;
    }

    @Override
    public void markieren(Abrechnung abrechnung) {
        leistungRepository.markData((app.entities.Abrechnung) abrechnung,
                (app.entities.Mandant) abrechnung.getMandant(), art);
    }

    @Override
    public BetragsBündel<Position> getBeträge(Abrechnung abrechnung) {
        MonetaryAmount betrag = getGebührenBasis(abrechnung);
        BetragsBündelMap<Position> bündel = new BetragsBündelMap<>();
        bündel.put(position, betrag);
        return bündel;
    }

    @Override
    public MonetaryAmount getGebührenBasis(Abrechnung abrechnung) {
        return leistungRepository.getBetrag(
                (app.entities.Abrechnung) abrechnung, art);
    }

    public void setLeistungsRepository(LeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

}
