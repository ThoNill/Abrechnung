package boundingContext.abrechnung.flow.handler;

import java.util.Optional;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.flow.payloads.AufrufPayload;
import boundingContext.abrechnung.repositories.AbrechnungRepository;

public class HoleAbrechnung extends
        AbstractPayloadTransformer<AufrufPayload, AbrechnungPayload> {

    private SachKontoProvider provider;

    public HoleAbrechnung(SachKontoProvider provider) {
        super();
        this.provider = provider;
    }

    @Override
    protected AbrechnungPayload transformPayload(AufrufPayload payload)
            throws Exception {
        checkPayload(payload);
        Abrechnung abrechnung = holeAbrechnung(payload);
        return new AbrechnungPayload(abrechnung, provider.getMandantRepository().save(abrechnung.getMandant()),
                payload.getArt());
    }

    private void checkPayload(AufrufPayload payload) {
        if (payload.getAbrechnungId() == 0 && (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN) || payload.getArt().equals(AbrechnungsArt.ERGÄNZEN))) {
            throw new IllegalArgumentException("Aktion erfordert eine Abrechnung");
        }
        if (payload.getAbrechnungId() > 0 && payload.getArt().equals(AbrechnungsArt.NEU)) {
            throw new IllegalArgumentException("Aktion darf keine Abrechnung enthalten");
        }
        prüfeObMandantInPayload(payload);
    }

    private Abrechnung holeAbrechnung(AufrufPayload payload) {
        Abrechnung abrechnung;
        if (payload.getAbrechnungId() > 0) {
            abrechnung = mitAbrechnungsId(payload);
        } else {
            abrechnung = ohneAbrechnungsId(payload);
        }
        return abrechnung;
    }

    private Abrechnung mitAbrechnungsId(AufrufPayload payload) {
        AbrechnungRepository abrechnungRepository = provider.getAbrechnungRepository();
        Abrechnung abrechnung = abrechnungRepository.findOne(payload
                .getAbrechnungId());
        prüfeAbrechnungPasstZumMandanten(payload, abrechnung);
        if (payload.getMonat() != abrechnung.getMonat() || payload.getJahr() != abrechnung.getJahr()) {
            throw new IllegalArgumentException("Monat oder Jahr passen nicht zur Abrechnung");
        }
        return abrechnung;
    }

    private void prüfeAbrechnungPasstZumMandanten(AufrufPayload payload,
            Abrechnung abrechnung) {
        if (abrechnung ==null) {
            throw new IllegalArgumentException("Die Abrechnung " + payload.getAbrechnungId() + " ist nicht vorhanden");
        }
        if (payload.getMandantId() > 0
                && payload.getMandantId() != abrechnung.getMandant()
                        .getMandantId()) {
            throw new IllegalArgumentException(
                    "Abrechnung und Mandant passen nicht zusammen");
        }
    }

    private Abrechnung ohneAbrechnungsId(AufrufPayload payload) {
        Mandant mandant = sucheMandant(payload);
        AbrechnungRepository abrechnungRepository = provider.getAbrechnungRepository();
        Optional<Abrechnung> oAbrechnung = mandant.getLetzteAbgerechneteAbrechnung(provider, payload.getMonat(),
                        payload.getJahr(), payload.getTyp());
        if (oAbrechnung.isPresent()) {
            return fallsEineAbrechnungSchonAbgerechnetWurde(payload,
                    oAbrechnung.get());
        } else {
            return ohneBereitsAbgerechneteAbrechnungen(payload, mandant);
        }
    }

    private void prüfeObMandantInPayload(AufrufPayload payload) {
        if (payload.getMandantId() == 0) {
            throw new IllegalArgumentException("MandantenId fehlt");
        }
    }

    private Mandant sucheMandant(AufrufPayload payload) {
        return provider.getMandantRepository().findOne(payload.getMandantId());
    }

    private Abrechnung fallsEineAbrechnungSchonAbgerechnetWurde(
            AufrufPayload payload, Abrechnung abrechnung) {
        return abrechnung.createOrGetNächsteAbrechnung(provider);
    }

    private Abrechnung ohneBereitsAbgerechneteAbrechnungen(
            AufrufPayload payload, Mandant mandant) {
        Integer n = provider.getAbrechnungRepository().getLetzteAbrechnung(mandant,
                payload.getMonat(), payload.getJahr());
        if (n == null) {
            keineNeueAbrechnung(payload);
            return mandant.createNeueAbrechnung(provider, payload.getMonat(),
                    payload.getJahr(), payload.getTyp());
        } else {
            return provider.getAbrechnungRepository().getAbrechnung(mandant, n.intValue())
                    .get(0);
        }
    }

    private void keineNeueAbrechnung(AufrufPayload payload) {
        if (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN) || payload.getArt().equals(AbrechnungsArt.ERGÄNZEN)) {
            throw new IllegalArgumentException("Aktion erfordert eine Abrechnung");
        }
    }

    public AbrechnungPayload testTransformPayload(AufrufPayload payload)
            throws Exception {
        return transformPayload(payload);
    }

}
