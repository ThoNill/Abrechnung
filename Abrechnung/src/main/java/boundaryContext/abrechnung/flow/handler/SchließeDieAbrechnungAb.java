package boundaryContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundaryContext.abrechnung.entities.Abrechnung;
import boundaryContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundaryContext.abrechnung.helper.AbrechnungAbschlieﬂen;
import boundaryContext.abrechnung.repositories.AbrechnungRepository;
import boundaryContext.abrechnung.repositories.BuchungRepository;
import boundaryContext.abrechnung.repositories.KontoBewegungRepository;

public class SchlieﬂeDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {

    private AbrechnungRepository abrechnungRepository;
    private BuchungRepository buchungRepository;
    private KontoBewegungRepository kontoBewegungRepository;

    public SchlieﬂeDieAbrechnungAb(AbrechnungRepository abrechnungRepository,
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
        AbrechnungAbschlieﬂen abchluss = new AbrechnungAbschlieﬂen(
                buchungRepository, kontoBewegungRepository,
                abrechnungRepository, 0.06);
        Abrechnung n‰chsteAbrechnung = abchluss.abschleiﬂen(
                payload.getAbrechnung(), 30);
        return payload;
    }
}
