package boundaryContext.abrechnung.helper;

import java.util.List;
import java.util.Optional;

import javax.validation.constraints.NotNull;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.entities.Mandant;
import boundaryContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.aufzählungen.AbrechnungsTyp;
import boundingContext.abrechnung.aufzählungen.RunStatus;

public class AbrechnungHelper {

    private AbrechnungRepository abrechnungRepository;

    public AbrechnungHelper(AbrechnungRepository abrechnungRepository) {
        super();
        this.abrechnungRepository = abrechnungRepository;
    }

    public Abrechnung createOrGetNächsteAbrechnung(@NotNull Abrechnung a) {
        List<Abrechnung> liste = abrechnungRepository.getAbrechnung(
                a.getMandant(), a.getNummer() + 1);
        if (liste.isEmpty()) {
            Abrechnung neu = new Abrechnung();
            neu.setNummer(a.getNummer() + 1);
            neu.setMandant(a.getMandant());
            neu.setMonat(a.getMonat());
            neu.setJahr(a.getJahr());
            neu.setTyp(a.getTyp());
            return abrechnungRepository.save(neu);
        }
        return liste.get(0);
    }

    public Optional<Abrechnung> getVorherigeAbrechnung(@NotNull Abrechnung a) {
        List<Abrechnung> liste = abrechnungRepository.getAbrechnung(
                a.getMandant(), a.getNummer() - 1);
        if (liste.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(liste.get(0));
    }

    public Optional<Abrechnung> getLetzteAbgerechneteAbrechnung(
            @NotNull Mandant mandant, int monat, int jahr, AbrechnungsTyp typ) {
        Integer n = abrechnungRepository.getLetzteAbgerechneteAbrechnung(
                mandant, RunStatus.ABGERECHNET, monat, jahr);
        if (n != null && n > 0) {
            List<Abrechnung> liste = abrechnungRepository.getAbrechnung(
                    mandant, n);
            return Optional.of(liste.get(0));
        }
        return Optional.empty();
    }

    public Abrechnung createNeueAbrechnung(Mandant mandant, int monat,
            int jahr, AbrechnungsTyp typ) {
        Integer n = abrechnungRepository.getLetzteAbrechnung(mandant);
        Abrechnung neu = new Abrechnung();
        neu.setNummer((n == null) ? 1 : n.intValue() + 1);
        neu.setMandant(mandant);
        neu.setMonat(monat);
        neu.setJahr(jahr);
        neu.setTyp(typ);
        return abrechnungRepository.save(neu);
    }

}
