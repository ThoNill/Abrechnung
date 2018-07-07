package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufz‰hlungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundingContext.abrechnung.helper.AbrechnungAbschlieﬂen;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;

public class SchlieﬂeDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {

    private AbrechnungRepository abrechnungRepository;
    private BuchungRepository buchungRepository;
    private KontoBewegungRepository kontoBewegungRepository;
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
    private SachKontoProvider sachKontoProvider;

    public SchlieﬂeDieAbrechnungAb(SachKontoProvider sachKontoProvider,
            AbrechnungRepository abrechnungRepository,
            BuchungRepository buchungRepository,
            KontoBewegungRepository kontoBewegungRepository,
            ZahlungsAuftragRepository zahlungsAuftragRepository) {
        super();
        this.sachKontoProvider = sachKontoProvider;
        this.abrechnungRepository = abrechnungRepository;
        this.buchungRepository = buchungRepository;
        this.kontoBewegungRepository = kontoBewegungRepository;
        this.zahlungsAuftragRepository = zahlungsAuftragRepository;
    }

    @Override
    protected AbrechnungPayload transformPayload(AbrechnungPayload payload)
            throws Exception {
        AbrechnungAbschlieﬂen abchluss = new AbrechnungAbschlieﬂen(
                sachKontoProvider, buchungRepository, kontoBewegungRepository,
                abrechnungRepository, zahlungsAuftragRepository, 0.06);
        Abrechnung n‰chsteAbrechnung = abchluss.abschleiﬂen(
                payload.getAbrechnung(), 30);
        return payload;
    }
}
