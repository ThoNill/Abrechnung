package tests.flow;

import javax.money.MonetaryAmount;

import boundingContext.abrechnung.aufz�hlungen.SachKonto;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.repositories.LeistungRepository;
import boundingContext.daten.Geb�hrRepository;
import boundingContext.gemeinsam.BetragsB�ndel;
import boundingContext.gemeinsam.BetragsB�ndelMap;

public class LeitungsGeb�hrRepository implements Geb�hrRepository<SachKonto> {

    private LeistungRepository leistungRepository;

    private SachKonto sachKonto;
    private int art;

    public LeitungsGeb�hrRepository(SachKonto sachKonto, int art) {
        super();
        this.sachKonto = sachKonto;
        this.art = art;
    }

    @Override
    public void markieren(Abrechnung abrechnung) {
        leistungRepository.markData(abrechnung, abrechnung.getMandant(), art);
    }

    @Override
    public BetragsB�ndel<SachKonto> getBetr�ge(Abrechnung abrechnung) {
        MonetaryAmount betrag = getGeb�hrenBasis(abrechnung);
        BetragsB�ndelMap<SachKonto> b�ndel = new BetragsB�ndelMap<>();
        b�ndel.put(sachKonto, betrag);
        return b�ndel;
    }

    @Override
    public MonetaryAmount getGeb�hrenBasis(Abrechnung abrechnung) {
        return leistungRepository.getBetrag(abrechnung, art);
    }

    public void setLeistungsRepository(LeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

}
