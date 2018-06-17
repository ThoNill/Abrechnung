package boundaryContext.abrechnung.flow.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.entities.Mandant;
import boundaryContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundaryContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundaryContext.abrechnung.flow.payloads.AufrufPayload;
import boundaryContext.abrechnung.helper.AbrechnungHelper;
import boundaryContext.abrechnung.repositories.AbrechnungRepository;
import boundaryContext.abrechnung.repositories.MandantRepository;

public class HoleAbrechnung extends
        AbstractPayloadTransformer<AufrufPayload, AbrechnungPayload> {

    private MandantRepository mandantRepository;

    private AbrechnungRepository abrechnungRepository;

    public HoleAbrechnung(MandantRepository mandantRepository,
            AbrechnungRepository abrechnungRepository) {
        super();
        this.mandantRepository = mandantRepository;
        this.abrechnungRepository = abrechnungRepository;
    }

    @Override
    protected AbrechnungPayload transformPayload(AufrufPayload payload)
            throws Exception {
        Abrechnung abrechnung = null;
        if (payload.getAbrechnungId() > 0) {
            abrechnung = abrechnungRepository
                    .findOne(payload.getAbrechnungId());
        } else {
            abrechnung = ohneAbrechnungsId(payload);
        }
        return new AbrechnungPayload(abrechnung, abrechnung.getMandant(),
                payload.getArt());
    }

    private Abrechnung ohneAbrechnungsId(AufrufPayload payload) {
        Mandant mandant = mandantRepository.findOne(payload.getMandantId());
        AbrechnungHelper h = new AbrechnungHelper(abrechnungRepository);
        Optional<Abrechnung> oAbrechnung = h.getLetzteAbgerechneteAbrechnung(
                mandant, payload.getMonat(), payload.getJahr(),
                payload.getTyp());
        Abrechnung abrechnung;
        if (oAbrechnung.isPresent()) {
            abrechnung = fallsEineAbrechnungSchonAbgerechnetWurde(payload, h,
                    oAbrechnung);
        } else {
            abrechnung = ohneBereitsAbgerechneteAbrechnungen(payload, mandant,
                    h);
        }
        return abrechnung;
    }

    private Abrechnung ohneBereitsAbgerechneteAbrechnungen(
            AufrufPayload payload, Mandant mandant, AbrechnungHelper h) {
        Abrechnung abrechnung;
        Integer n = abrechnungRepository.getLetzteAbrechnung(mandant,
                payload.getMonat(), payload.getJahr());
        if (n == null) {
            abrechnung = h.createNeueAbrechnung(mandant, payload.getMonat(),
                    payload.getJahr(), payload.getTyp());
        } else {
            List<Abrechnung> lAbrechnung = abrechnungRepository.getAbrechnung(
                    mandant, n.intValue());
            abrechnung = lAbrechnung.get(0);
        }
        return abrechnung;
    }

    private Abrechnung fallsEineAbrechnungSchonAbgerechnetWurde(
            AufrufPayload payload, AbrechnungHelper h,
            Optional<Abrechnung> oAbrechnung) {
        Abrechnung abrechnung;
        if (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN)) {
            abrechnung = oAbrechnung.get();
        } else {
            abrechnung = h.createOrGetNächsteAbrechnung(oAbrechnung.get());
        }
        return abrechnung;
    }

}
