package boundingContext.buchhaltung.eingang;

import java.util.Optional;

import javax.money.MonetaryAmount;

import mathe.bündel.GruppenBündel;
import mathe.paare.Paar;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.AbrechnungsRepository;
import boundingContext.buchhaltung.BuchhaltungsRepository;
import boundingContext.gemeinsam.BetragsBündelMap;
import ddd.Service;

public class BuchhaltungService<ID, KEY> implements Service {
    private BuchhaltungsRepository buchhaltungsRepository;
    private AbrechnungsRepository<ID> abrechnungsRepository;

    public BuchhaltungService(BuchhaltungsRepository buchhaltungsRepository) {
        super();
        this.buchhaltungsRepository = buchhaltungsRepository;
    }

    public void einbuchen(BuchungsAufträge<ID, KEY> aufträge) {
        Abrechnung<ID> abrechnung = (Abrechnung<ID>) aufträge.getBeteiligte()
                .getValue("Abrechung");

        BetragsBündelMap<Paar<Beschreibung, KEY>> neueSalden = new BetragsBündelMap<>(
                aufträge.inEinPaarBündelumwandeln());
        BetragsBündelMap<Paar<Beschreibung, KEY>> salden = buchhaltungsRepository
                .getSalden(abrechnung);

        GruppenBündel<Paar<Beschreibung, KEY>, MonetaryAmount> differenz = neueSalden
                .subtract(salden, neueSalden);
        // begrenzte Differenz;

        GruppenBündel<Paar<Beschreibung, KEY>, MonetaryAmount> neuerKontostand = salden
                .erweitern(neueSalden);
        MonetaryAmount saldo = neuerKontostand.getSumme();

        if (saldo.isNegative()) {
            überzahlung(saldo, neuerKontostand);
            überzahlungInDieNächsteAbrechnung(saldo,
                    abrechnungsRepository.getFolgendeAbrechnung(abrechnung));
        } else {
            erzeugeRestzahlungen(saldo, neuerKontostand);
        }

        // berechne.erweitern(konvertiert);

        // salden.subtract(salden, b);
    }

    private void überzahlungInDieNächsteAbrechnung(MonetaryAmount saldo,
            Optional<Abrechnung<ID>> folgendeAbrechnung) {
        // TODO Auto-generated method stub

    }

    private void erzeugeRestzahlungen(
            MonetaryAmount saldo,
            GruppenBündel<Paar<Beschreibung, KEY>, MonetaryAmount> neuerKontostand) {
        // TODO Auto-generated method stub

    }

    private void überzahlung(
            MonetaryAmount saldo,
            GruppenBündel<Paar<Beschreibung, KEY>, MonetaryAmount> neuerKontostand) {
        // TODO Auto-generated method stub

    };
}
