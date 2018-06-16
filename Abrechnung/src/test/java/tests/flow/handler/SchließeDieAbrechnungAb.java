package tests.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import tests.AbrechnungAbschlie�en;
import tests.flow.payloads.AbrechnungPayload;
import app.entities.Abrechnung;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;

public class Schlie�eDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {

    private AbrechnungRepository abrechnungRepository;
    private BuchungRepository buchungRepository;
    private KontoBewegungRepository kontoBewegungRepository;

    public Schlie�eDieAbrechnungAb(AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository) {
        super();
        this.abrechnungRepository = abrechnungRepository;
        this.buchungRepository = buchungRepository;
        this.kontoBewegungRepository = kontoBewegungRepository;
    }

    @Override
    protected AbrechnungPayload transformPayload(AbrechnungPayload payload)
            throws Exception {
        AbrechnungAbschlie�en abchluss = new AbrechnungAbschlie�en(
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, 0.06);
        Abrechnung n�chsteAbrechnung = abchluss.abschlei�en(
                payload.getAbrechnung(), 30);
        return payload;
    }
}
