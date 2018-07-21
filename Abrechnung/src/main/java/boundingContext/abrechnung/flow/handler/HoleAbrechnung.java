package boundingContext.abrechnung.flow.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.actions.AbrechnungHelper;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.flow.payloads.AufrufPayload;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;

public class HoleAbrechnung extends
        AbstractPayloadTransformer<AufrufPayload, AbrechnungPayload> {

    private MandantRepository mandantRepository;

    private AbrechnungRepository abrechnungRepository;

    AbrechnungHelper helper;

    public HoleAbrechnung(MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository) {
        super();
        this.mandantRepository = mandantRepository;
        this.abrechnungRepository = abrechnungRepository;
        this.helper = new AbrechnungHelper(abrechnungRepository);
    }

    @Override
    protected AbrechnungPayload transformPayload(AufrufPayload payload)
            throws Exception {
        checkPayload(payload);
        Abrechnung abrechnung = holeAbrechnung(payload);
        return new AbrechnungPayload(abrechnung, abrechnung.getMandant(),
                payload.getArt());
    }

    private void checkPayload(AufrufPayload payload) {
        if (payload.getAbrechnungId() == 0 && (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN) || payload.getArt().equals(AbrechnungsArt.ERG�NZEN))) {
            throw new IllegalArgumentException("Aktion erfordert eine Abrechnung");
        }
        if (payload.getAbrechnungId() > 0 && payload.getArt().equals(AbrechnungsArt.NEU)) {
            throw new IllegalArgumentException("Aktion darf keine Abrechnung enthalten");
        }
        pr�feObMandantInPayload(payload);
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
        Abrechnung abrechnung = abrechnungRepository.findOne(payload
                .getAbrechnungId());
        pr�feAbrechnungPasstZumMandanten(payload, abrechnung);
        if (payload.getMonat() != abrechnung.getMonat() || payload.getJahr() != abrechnung.getJahr()) {
            throw new IllegalArgumentException("Monat oder Jahr passen nicht zur Abrechnung");
        }
        return abrechnung;
    }

    private void pr�feAbrechnungPasstZumMandanten(AufrufPayload payload,
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

        Optional<Abrechnung> oAbrechnung = helper
                .getLetzteAbgerechneteAbrechnung(mandant, payload.getMonat(),
                        payload.getJahr(), payload.getTyp());
        if (oAbrechnung.isPresent()) {
            return fallsEineAbrechnungSchonAbgerechnetWurde(payload,
                    oAbrechnung.get());
        } else {
            return ohneBereitsAbgerechneteAbrechnungen(payload, mandant);
        }
    }

    private void pr�feObMandantInPayload(AufrufPayload payload) {
        if (payload.getMandantId() == 0) {
            throw new IllegalArgumentException("MandantenId fehlt");
        }
    }

    private Mandant sucheMandant(AufrufPayload payload) {
        return mandantRepository.findOne(payload.getMandantId());
    }

    private Abrechnung fallsEineAbrechnungSchonAbgerechnetWurde(
            AufrufPayload payload, Abrechnung abrechnung) {
        return helper.createOrGetN�chsteAbrechnung(abrechnung);
    }

    private Abrechnung ohneBereitsAbgerechneteAbrechnungen(
            AufrufPayload payload, Mandant mandant) {
        Integer n = abrechnungRepository.getLetzteAbrechnung(mandant,
                payload.getMonat(), payload.getJahr());
        if (n == null) {
            keineNeueAbrechnung(payload);
            return helper.createNeueAbrechnung(mandant, payload.getMonat(),
                    payload.getJahr(), payload.getTyp());
        } else {
            return abrechnungRepository.getAbrechnung(mandant, n.intValue())
                    .get(0);
        }
    }

    private void keineNeueAbrechnung(AufrufPayload payload) {
        if (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN) || payload.getArt().equals(AbrechnungsArt.ERG�NZEN)) {
            throw new IllegalArgumentException("Aktion erfordert eine Abrechnung");
        }
    }

    public AbrechnungPayload testTransformPayload(AufrufPayload payload)
            throws Exception {
        return transformPayload(payload);
    }

}
