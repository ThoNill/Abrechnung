package boundingContext.abrechnung.flow.handler;

import org.springframework.integration.transformer.AbstractPayloadTransformer;

import boundingContext.abrechnung.aufzählungen.SachKontoProvider;
import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.flow.payloads.AbrechnungPayload;
import boundingContext.abrechnung.helper.AbrechnungAbschließen;
import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;

public class SchließeDieAbrechnungAb extends
        AbstractPayloadTransformer<AbrechnungPayload, AbrechnungPayload> {

    private AbrechnungRepository abrechnungRepository;
    private BuchungRepository buchungRepository;
    private KontoBewegungRepository kontoBewegungRepository;
    private ZahlungsAuftragRepository zahlungsAuftragRepository;
    private SachKontoProvider sachKontoProvider;

    public SchließeDieAbrechnungAb(SachKontoProvider sachKontoProvider,
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
        AbrechnungAbschließen abchluss = new AbrechnungAbschließen(
                sachKontoProvider, buchungRepository, kontoBewegungRepository,
                abrechnungRepository, zahlungsAuftragRepository, 0.06);
        Abrechnung nächsteAbrechnung = abchluss.abschleißen(
                payload.getAbrechnung(), 30);
        return payload;
    }
}
