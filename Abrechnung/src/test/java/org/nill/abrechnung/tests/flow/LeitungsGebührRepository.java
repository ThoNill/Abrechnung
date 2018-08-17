package org.nill.abrechnung.tests.flow;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufzählungen.SachKonto;
import org.nill.abrechnung.interfaces.GebührRepository;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.repositories.ILeistungRepository;
import org.nill.basiskomponenten.gemeinsam.BetragsBündel;
import org.nill.basiskomponenten.gemeinsam.BetragsBündelMap;

public class LeitungsGebührRepository implements GebührRepository<SachKonto> {

    private ILeistungRepository leistungRepository;

    private SachKonto sachKonto;
    private int art;

    public LeitungsGebührRepository(SachKonto sachKonto, int art) {
        super();
        this.sachKonto = sachKonto;
        this.art = art;
    }

    @Override
    public void markieren(IAbrechnung abrechnung) {
        leistungRepository.markData(abrechnung, abrechnung.getMandant(), art);
    }

    @Override
    public BetragsBündel<SachKonto> getBeträge(IAbrechnung abrechnung) {
        MonetaryAmount betrag = getGebührenBasis(abrechnung);
        BetragsBündelMap<SachKonto> bündel = new BetragsBündelMap<>();
        bündel.put(sachKonto, betrag);
        return bündel;
    }

    @Override
    public MonetaryAmount getGebührenBasis(IAbrechnung abrechnung) {
        return leistungRepository.getBetrag(abrechnung, art);
    }

    public void setLeistungsRepository(ILeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

}
