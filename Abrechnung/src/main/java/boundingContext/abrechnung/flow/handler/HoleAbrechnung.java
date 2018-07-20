package boundingContext.abrechnung.flow.handler;

import java.util.List;
import java.util.Optional;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.Mandant;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundingContext.abrechnung.flow.payloads.AbrechnungsArt;
import boundingContext.abrechnung.flow.payloads.AufrufPayload;
import boundingContext.abrechnung.helper.AbrechnungHelper;
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
        Abrechnung abrechnung = holeAbrechnung(payload);
        return new AbrechnungPayload(abrechnung, abrechnung.getMandant(),
                payload.getArt());
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
        Abrechnung abrechnung = abrechnungRepository.findOne(payload.getAbrechnungId());
        prüfeAbrechnungPasstZumMandanten(payload, abrechnung);
        return abrechnung;
    }

    private void prüfeAbrechnungPasstZumMandanten(AufrufPayload payload,
            Abrechnung abrechnung) {
        if (payload.getMandantId()>0 && payload.getMandantId() != abrechnung.getMandant().getMandantId()) {
            throw new IllegalArgumentException("Abrechnung und Mandant passen nicht zusammen");
        }
    }

    private Abrechnung ohneAbrechnungsId(AufrufPayload payload) {
        prüfeObMandantInPayload(payload);
        Mandant mandant = sucheMandant(payload);
        
        Optional<Abrechnung> oAbrechnung = helper.getLetzteAbgerechneteAbrechnung(
                mandant, payload.getMonat(), payload.getJahr(),
                payload.getTyp());
        if (oAbrechnung.isPresent()) {
            return fallsEineAbrechnungSchonAbgerechnetWurde(payload, 
                    oAbrechnung.get());
        } else {
            return ohneBereitsAbgerechneteAbrechnungen(payload, mandant);
        }
    }

    private void prüfeObMandantInPayload(AufrufPayload payload) {
        if (payload.getMandantId()==0) {
            throw new IllegalArgumentException("MandantenId fehlt");
        }
    }

    private Mandant sucheMandant(AufrufPayload payload) {
        return mandantRepository.findOne(payload.getMandantId());
    }


    private Abrechnung fallsEineAbrechnungSchonAbgerechnetWurde(
            AufrufPayload payload,Abrechnung abrechnung) {
        if (payload.getArt().equals(AbrechnungsArt.NACHBERCHNEN)) {
            return abrechnung;
        } else {
            return helper.createOrGetNächsteAbrechnung(abrechnung);
        }
    }

    private Abrechnung ohneBereitsAbgerechneteAbrechnungen(
            AufrufPayload payload, Mandant mandant) {
        Integer n = abrechnungRepository.getLetzteAbrechnung(mandant,payload.getMonat(), payload.getJahr());
        if (n == null) {
            return helper.createNeueAbrechnung(mandant, payload.getMonat(),
                    payload.getJahr(), payload.getTyp());
        } else {
            return abrechnungRepository.getAbrechnung(mandant, n.intValue()).get(0);
        }
    }
}
