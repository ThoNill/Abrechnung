package boundingContext.abrechnung.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import boundingContext.abrechnung.AbrechnungsDatenRepository;
import boundingContext.abrechnung.AbrechnungsDatenRepositoryFabrik;
import boundingContext.abrechnung.TeilAbrechnung;
import boundingContext.abrechnung.TeilAbrechnungsBeschreibung;
import boundingContext.abrechnung.TeilAbrechnungsListe;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.AbrechnungsFabrik;
import boundingContext.abrechnung.entities.AbrechnungsRepository;
import boundingContext.abrechnung.entities.AbrechnungsStatus;
import boundingContext.abrechnung.entities.AbrechnungsTyp;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.entities.MandantenRepository;
import boundingContext.abrechnung.entities.RunStatus;
import boundingContext.abrechnung.entities.Zeitraum;
import boundingContext.buchhaltung.eingang.BuchhaltungService;
import boundingContext.buchhaltung.eingang.BuchungsAuftr�ge;
import ddd.Service;

public class AbrechnungsService<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY extends AbrechnungsDatenRepository<ID>>
        implements Service {
    private AbrechnungsRepository<ID> abrechnungsRepository;
    private MandantenRepository<ID, GEB�HRENDEFINITION> mandantenRepository;
    private AbrechnungsFabrik<ID> abrechnungsFabrik;
    private AbrechnungsDatenRepositoryFabrik<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> abrechnungsDatenRepositoryFabrik;
    private BuchhaltungService<ID, KEY> buchhaltungsService;

    public AbrechnungsService(
            AbrechnungsRepository<ID> abrechnungsRepository,
            MandantenRepository<ID, GEB�HRENDEFINITION> mandantenRepository,
            AbrechnungsFabrik<ID> abrechnungsFabrik,
            AbrechnungsDatenRepositoryFabrik<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> abrechnungsDatenRepositoryFabrik,
            BuchhaltungService<ID, KEY> buchhaltungsService) {
        super();
        this.abrechnungsRepository = Objects
                .requireNonNull(abrechnungsRepository);
        this.mandantenRepository = Objects.requireNonNull(mandantenRepository);
        this.abrechnungsFabrik = Objects.requireNonNull(abrechnungsFabrik);
        this.abrechnungsDatenRepositoryFabrik = Objects
                .requireNonNull(abrechnungsDatenRepositoryFabrik);
        this.buchhaltungsService = Objects.requireNonNull(buchhaltungsService);
    }

    public void mehrereMandantenAbrechnen(Iterable<ID> mandantenIDs,
            Zeitraum zeitraum, AbrechnungsAktion aktion, AbrechnungsTyp typ) {
        Objects.requireNonNull(mandantenIDs);

        mandantenIDs.forEach(x -> einenMandantenAbrechnen(x, zeitraum, aktion,
                typ));
    }

    public void einenMandantenAbrechnen(ID mandantenID, Zeitraum zeitraum,
            AbrechnungsAktion aktion, AbrechnungsTyp typ) {
        Objects.requireNonNull(zeitraum);

        Abrechnung<ID> abrechnung = bestimmeAbrechnung(mandantenID, zeitraum,
                typ);
        pr�fenObDerStatusZuAktionPasst(aktion, abrechnung);
        abrechnung.setRunStatus(RunStatus.RUNNING);
        abrechnungSpeichern(abrechnung);

        DATENREPOSITORY repo = geeignetesRepositoryBestimmen(abrechnung, aktion);

        repo.markiereDaten(abrechnung);

        GEB�HRENDEFINITION geb�hrenDefinition = holeGeb�hrenDefinition(abrechnung);

        TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> teilAbrechnungen = listeDertTeilabrechnungenErzeugen(geb�hrenDefinition);
        BuchungsAuftr�ge<ID, KEY> isolierteAuftr�ge = teilAbrechnungen
                .abrechnen(repo, abrechnung);

        TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>> sammelAbrechnungen = listeDerSammelAbrechnungenErzeugen(geb�hrenDefinition);
        BuchungsAuftr�ge<ID, KEY> sammelAuftr�ge = sammelAbrechnungen
                .abrechnen(isolierteAuftr�ge, abrechnung);

        BuchungsAuftr�ge<ID, KEY> alleAuftr�ge = new BuchungsAuftr�ge<>(
                isolierteAuftr�ge.getBeteiligte());
        alleAuftr�ge.addAll(isolierteAuftr�ge);
        alleAuftr�ge.addAll(sammelAuftr�ge);
    }

    private GEB�HRENDEFINITION holeGeb�hrenDefinition(Abrechnung<ID> abrechnung) {

        Mandant<ID> mandant = abrechnung.getMandant();
        Optional<GEB�HRENDEFINITION> geb�hrenDefinition = mandantenRepository
                .getGeb�hrenDefinition(mandant);

        if (!geb�hrenDefinition.isPresent()) {
            throw new IllegalStateException(
                    "Es gibt keine Geb�hrendefinition f�r Mandant "
                            + mandant.getId());
        }

        return geb�hrenDefinition.get();
    }

    private TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> listeDertTeilabrechnungenErzeugen(
            GEB�HRENDEFINITION geb�hrenDefinition) {

        List<TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, DATENREPOSITORY>> beschreibungen = abrechnungsDatenRepositoryFabrik
                .getTeilAbrechnungsBeschreibungen();

        TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> teilAbrechnungen = new TeilAbrechnungsListe<>();
        for (TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, DATENREPOSITORY> beschreibung : beschreibungen) {
            TeilAbrechnung<ID, KEY, GEB�HRENDEFINITION, DATENREPOSITORY> teilAbrechnung = new TeilAbrechnung<>(
                    geb�hrenDefinition, beschreibung);
            teilAbrechnungen.add(teilAbrechnung);
        }
        return teilAbrechnungen;
    }

    private TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>> listeDerSammelAbrechnungenErzeugen(
            GEB�HRENDEFINITION geb�hrenDefinition) {

        List<TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>>> beschreibungen = abrechnungsDatenRepositoryFabrik
                .getSammelAbrechnungsBeschreibungen();

        TeilAbrechnungsListe<ID, KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>> teilAbrechnungen = new TeilAbrechnungsListe<>();
        for (TeilAbrechnungsBeschreibung<KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>> beschreibung : beschreibungen) {
            TeilAbrechnung<ID, KEY, GEB�HRENDEFINITION, BuchungsAuftr�ge<ID, KEY>> teilAbrechnung = new TeilAbrechnung<>(
                    geb�hrenDefinition, beschreibung);
            teilAbrechnungen.add(teilAbrechnung);
        }
        return teilAbrechnungen;
    }

    private void abrechnungSpeichern(Abrechnung<ID> abrechnung) {

        if (AbrechnungsStatus.INITIAL.equals(abrechnung.getStatus())) {
            abrechnung.setStatus(AbrechnungsStatus.NICHTBEBUCHT);
            abrechnungsRepository.insert(abrechnung);
        } else {
            abrechnungsRepository.update(abrechnung);
        }
    }

    private void pr�fenObDerStatusZuAktionPasst(AbrechnungsAktion aktion,
            Abrechnung<ID> abrechnung) {

        if (AbrechnungsStatus.STORNIERT.equals(abrechnung.getStatus())) {
            throw new IllegalArgumentException(
                    "Abrechnungen sollte bei einer Abrechnung nicht storniert sein");
        }
        if (AbrechnungsAktion.NACHBERECHNEN.equals(aktion)
                && !AbrechnungsStatus.ABGERECHNET
                        .equals(abrechnung.getStatus())) {
            throw new IllegalArgumentException(
                    "Nur abgerechnete Abrechnungen k�nnen nachberechnet werden");
        }
        if (AbrechnungsAktion.ERG�NZEN.equals(aktion)
                && !AbrechnungsStatus.ABGERECHNET
                        .equals(abrechnung.getStatus())) {
            throw new IllegalArgumentException(
                    "Nur abgerechnete Abrechnungen k�nnen erg�nzt werden");
        }
        if (AbrechnungsAktion.STORNIEREN.equals(aktion)
                && !AbrechnungsStatus.ABGERECHNET
                        .equals(abrechnung.getStatus())) {
            throw new IllegalArgumentException(
                    "Nur abgerechnete Abrechnungen k�nnen storniert werden");
        }
        if (AbrechnungsAktion.ERSTABRECHNUNG.equals(aktion)
                && AbrechnungsStatus.ABGERECHNET.equals(abrechnung.getStatus())) {
            throw new IllegalArgumentException(
                    "Bei einer Erstabrechnung darf die Abrechnung noch nicht abgerechnet sein");
        }
    }

    private Abrechnung<ID> bestimmeAbrechnung(ID mandantenID,
            Zeitraum zeitraum, AbrechnungsTyp typ) {
        Optional<Mandant<ID>> optMandant = mandantenRepository
                .getMandant(mandantenID);
        if (optMandant.isPresent()) {
            return bestimmeAbrechnung(optMandant.get(), zeitraum, typ);
        } else {
            throw new IllegalArgumentException("Der Mandant mit ID "
                    + mandantenID + " ist nicht vorhanden");
        }
    }

    private Abrechnung<ID> bestimmeAbrechnung(Mandant<ID> mandant,
            Zeitraum zeitraum, AbrechnungsTyp typ) {
        Optional<Abrechnung<ID>> optAbrechnung = abrechnungsRepository
                .aktuelleAbrechnung(mandant, zeitraum);
        if (optAbrechnung.isPresent()) {
            if (!typ.equals(optAbrechnung.get().getTyp())) {
                return abrechnungsFabrik.create(mandant, zeitraum, typ);
            }
            if (AbrechnungsStatus.STORNIERT.equals(optAbrechnung.get()
                    .getStatus())) {
                return abrechnungsFabrik.create(mandant, zeitraum, typ);
            }
            return optAbrechnung.get();
        } else {
            return abrechnungsFabrik.create(mandant, zeitraum, typ);
        }
    }

    private DATENREPOSITORY geeignetesRepositoryBestimmen(
            Abrechnung<ID> abrechnung, AbrechnungsAktion aktion) {
        return (AbrechnungsStatus.ABGERECHNET.equals(abrechnung.getStatus()) && !AbrechnungsAktion.ERG�NZEN
                .equals(aktion)) ? abrechnungsDatenRepositoryFabrik
                .nachberechnungsRepository(abrechnung)
                : abrechnungsDatenRepositoryFabrik
                        .markierendesRepository(abrechnung);
    }

}
