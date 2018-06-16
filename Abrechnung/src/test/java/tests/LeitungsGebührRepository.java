package tests;

import javax.money.MonetaryAmount;

import app.repositories.LeistungRepository;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.daten.Geb�hrRepository;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;

public class LeitungsGeb�hrRepository implements Geb�hrRepository<Position> {

    private LeistungRepository leistungRepository;

    private Position position;
    private int art;

    public LeitungsGeb�hrRepository(Position position, int art) {
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
    public BetragsB�ndel<Position> getBetr�ge(Abrechnung abrechnung) {
        MonetaryAmount betrag = getGeb�hrenBasis(abrechnung);
        BetragsB�ndelMap<Position> b�ndel = new BetragsB�ndelMap<>();
        b�ndel.put(position, betrag);
        return b�ndel;
    }

    @Override
    public MonetaryAmount getGeb�hrenBasis(Abrechnung abrechnung) {
        return leistungRepository.getBetrag(
                (app.entities.Abrechnung) abrechnung, art);
    }

    public void setLeistungsRepository(LeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

}
