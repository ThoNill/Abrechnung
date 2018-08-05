package org.nill.abrechnung.flow.handler;

import java.util.Optional;

import org.nill.abrechnung.aufz�hlungen.SachKontoProvider;
import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.Mandant;
import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.flow.payloads.AbrechnungsArt;
import org.nill.abrechnung.flow.payloads.AufrufPayload;
import org.nill.abrechnung.repositories.AbrechnungRepository;
import org.springframework.integration.transformer.AbstractPayloadTransformer;

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
        return new AbrechnungPayload(abrechnung, provider
                .getMandantRepository().save(abrechnung.getMandant()),
                payload.getArt());
    }

    private void checkPayload(AufrufPayload payload) {
        if (payload.getAbrechnungId() == 0
                && (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN) || payload
                        .getArt().equals(AbrechnungsArt.ERG�NZEN))) {
            throw new IllegalArgumentException(
                    "Aktion erfordert eine Abrechnung");
        }
        if (payload.getAbrechnungId() > 0
                && payload.getArt().equals(AbrechnungsArt.NEU)) {
            throw new IllegalArgumentException(
                    "Aktion darf keine Abrechnung enthalten");
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
        AbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
        Abrechnung abrechnung = abrechnungRepository.findOne(payload
                .getAbrechnungId());
        pr�feAbrechnungPasstZumMandanten(payload, abrechnung);
        if (!payload.getMj().equals(abrechnung.getMj())) {
            throw new IllegalArgumentException(
                    "Monat oder Jahr passen nicht zur Abrechnung");
        }
        return abrechnung;
    }

    private void pr�feAbrechnungPasstZumMandanten(AufrufPayload payload,
            Abrechnung abrechnung) {
        if (abrechnung == null) {
            throw new IllegalArgumentException("Die Abrechnung "
                    + payload.getAbrechnungId() + " ist nicht vorhanden");
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
        AbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
        Optional<Abrechnung> oAbrechnung = mandant
                .getLetzteAbgerechneteAbrechnung(provider, payload.getMj(),
                        payload.getTyp());
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
        return provider.getMandantRepository().findOne(payload.getMandantId());
    }

    private Abrechnung fallsEineAbrechnungSchonAbgerechnetWurde(
            AufrufPayload payload, Abrechnung abrechnung) {
        return abrechnung.createOrGetN�chsteAbrechnung(provider);
    }

    private Abrechnung ohneBereitsAbgerechneteAbrechnungen(
            AufrufPayload payload, Mandant mandant) {
        Integer n = provider.getAbrechnungRepository().getLetzteAbrechnung(
                mandant, payload.getMj());
        if (n == null) {
            keineNeueAbrechnung(payload);
            return mandant.createNeueAbrechnung(provider, payload.getMj(),
                    payload.getTyp());
        } else {
            return provider.getAbrechnungRepository()
                    .getAbrechnung(mandant, n.intValue()).get(0);
        }
    }

    private void keineNeueAbrechnung(AufrufPayload payload) {
        if (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN)
                || payload.getArt().equals(AbrechnungsArt.ERG�NZEN)) {
            throw new IllegalArgumentException(
                    "Aktion erfordert eine Abrechnung");
        }
    }

    public AbrechnungPayload testTransformPayload(AufrufPayload payload)
            throws Exception {
        return transformPayload(payload);
    }

}