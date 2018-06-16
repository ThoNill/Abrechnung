package tests.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import tests.AbrechnungAbschließen;
import tests.flow.payloads.AbrechnungPayload;
import app.entities.Abrechnung;
import app.repositories.AbrechnungRepository;
import app.repositories.BuchungRepository;
import app.repositories.KontoBewegungRepository;

public class SchließeDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {

    private AbrechnungRepository abrechnungRepository;
    private BuchungRepository buchungRepository;
    private KontoBewegungRepository kontoBewegungRepository;

    public SchließeDieAbrechnungAb(AbrechnungRepository abrechnungRepository,
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
        AbrechnungAbschließen abchluss = new AbrechnungAbschließen(
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, 0.06);
        Abrechnung nächsteAbrechnung = abchluss.abschleißen(
                payload.getAbrechnung(), 30);
        return payload;
    }
}
