package org.nill.abrechnung.tests.flow;

import javax.money.MonetaryAmount;

import org.nill.abrechnung.aufz�hlungen.SachKonto;
import org.nill.abrechnung.interfaces.Geb�hrRepository;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.repositories.ILeistungRepository;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndel;
import org.nill.basiskomponenten.gemeinsam.BetragsB�ndelMap;

public class LeitungsGeb�hrRepository implements Geb�hrRepository<SachKonto> {

    private ILeistungRepository leistungRepository;

    private SachKonto sachKonto;
    private int art;

    public LeitungsGeb�hrRepository(SachKonto sachKonto, int art) {
        super();
        this.sachKonto = sachKonto;
        this.art = art;
    }

    @Override
    public void markieren(IAbrechnung abrechnung) {
        leistungRepository.markData(abrechnung, abrechnung.getMandant(), art);
    }

    @Override
    public BetragsB�ndel<SachKonto> getBetr�ge(IAbrechnung abrechnung) {
        MonetaryAmount betrag = getGeb�hrenBasis(abrechnung);
        BetragsB�ndelMap<SachKonto> b�ndel = new BetragsB�ndelMap<>();
        b�ndel.put(sachKonto, betrag);
        return b�ndel;
    }

    @Override
    public MonetaryAmount getGeb�hrenBasis(IAbrechnung abrechnung) {
        return leistungRepository.getBetrag(abrechnung, art);
    }

    public void setLeistungsRepository(ILeistungRepository leistungRepository) {
        this.leistungRepository = leistungRepository;
    }

}
