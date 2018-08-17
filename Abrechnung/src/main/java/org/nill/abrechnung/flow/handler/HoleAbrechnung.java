package org.nill.abrechnung.flow.handler;

import java.util.Optional;

import org.nill.abrechnung.aufzählungen.AbrechnungsArt;
import org.nill.abrechnung.flow.payloads.AbrechnungPayload;
import org.nill.abrechnung.flow.payloads.AufrufPayload;
import org.nill.abrechnung.interfaces.IAbrechnung;
import org.nill.abrechnung.interfaces.IAbrechnungRepository;
import org.nill.abrechnung.interfaces.IMandant;
import org.nill.abrechnung.interfaces.SachKontoProvider;
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
        IAbrechnung abrechnung = holeAbrechnung(payload);
        return new AbrechnungPayload(abrechnung, provider
                .getMandantRepository().save(abrechnung.getMandant()),
                payload.getArt());
    }

    private void checkPayload(AufrufPayload payload) {
        if (payload.getAbrechnungId() == 0
                && (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN) || payload
                        .getArt().equals(AbrechnungsArt.ERGÄNZEN))) {
            throw new IllegalArgumentException(
                    "Aktion erfordert eine Abrechnung");
        }
        if (payload.getAbrechnungId() > 0
                && payload.getArt().equals(AbrechnungsArt.NEU)) {
            throw new IllegalArgumentException(
                    "Aktion darf keine Abrechnung enthalten");
        }
        prüfeObMandantInPayload(payload);
    }

    private IAbrechnung holeAbrechnung(AufrufPayload payload) {
        IAbrechnung abrechnung;
        if (payload.getAbrechnungId() > 0) {
            abrechnung = mitAbrechnungsId(payload);
        } else {
            abrechnung = ohneAbrechnungsId(payload);
        }
        return abrechnung;
    }

    private IAbrechnung mitAbrechnungsId(AufrufPayload payload) {
        IAbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
        IAbrechnung abrechnung = abrechnungRepository.findOne(payload
                .getAbrechnungId());
        prüfeAbrechnungPasstZumMandanten(payload, abrechnung);
        if (!payload.getMj().equals(abrechnung.getMj())) {
            throw new IllegalArgumentException(
                    "Monat oder Jahr passen nicht zur Abrechnung");
        }
        return abrechnung;
    }

    private void prüfeAbrechnungPasstZumMandanten(AufrufPayload payload,
            IAbrechnung abrechnung) {
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

    private IAbrechnung ohneAbrechnungsId(AufrufPayload payload) {
        IMandant mandant = sucheMandant(payload);
        IAbrechnungRepository abrechnungRepository = provider
                .getAbrechnungRepository();
        Optional<IAbrechnung> oAbrechnung = mandant
                .getLetzteAbgerechneteAbrechnung(provider, payload.getMj(),
                        payload.getTyp());
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

    private IMandant sucheMandant(AufrufPayload payload) {
        return provider.getMandantRepository().findOne(payload.getMandantId());
    }

    private IAbrechnung fallsEineAbrechnungSchonAbgerechnetWurde(
            AufrufPayload payload, IAbrechnung abrechnung) {
        return abrechnung.createOrGetNächsteAbrechnung(provider);
    }

    private IAbrechnung ohneBereitsAbgerechneteAbrechnungen(
            AufrufPayload payload, IMandant mandant) {
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
                || payload.getArt().equals(AbrechnungsArt.ERGÄNZEN)) {
            throw new IllegalArgumentException(
                    "Aktion erfordert eine Abrechnung");
        }
    }

    public AbrechnungPayload testTransformPayload(AufrufPayload payload)
            throws Exception {
        return transformPayload(payload);
    }

}
