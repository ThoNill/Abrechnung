package boundingContext.abrechnung.aufz�hlungen;

import org.springframework.beans.factory.annotation.Autowired;

import boundingContext.abrechnung.repositories.AbrechnungRepository;
import boundingContext.abrechnung.repositories.BuchungRepository;
import boundingContext.abrechnung.repositories.KontoBewegungRepository;
import boundingContext.abrechnung.repositories.MandantRepository;
import boundingContext.abrechnung.repositories.ZahlungsAuftragRepository;
import boundingContext.abrechnung.repositories.ZahlungsDefinitionRepository;
import boundingContext.abrechnung.repositories.�berweisungRepository;

public interface SachKontoProvider extends RepositoryProvider {
    SachKonto GEB�HR();

    SachKonto GUTHABEN();
    
    SachKonto AUSZUZAHLEN();

    SachKonto AUSBEZAHLT();

    SachKonto SCHULDEN();

    SachKonto ZINS();

    SachKonto MWST();

    SachKonto sachKontoFrom(int pos);
    
    default int ABGLEICH_GUTHABEN() {
        return BuchungsArt.ABGLEICH_GUTHABEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int �BERNAHME_SCHULDEN = 4;
    };
    
    default int ABGLEICH_SCHULDEN() {
        return BuchungsArt.ABGLEICH_SCHULDEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int �BERNAHME_SCHULDEN = 4;
    };
 
    default int �BERNAHME_SCHULDEN() {
        return BuchungsArt.�BERNAHME_SCHULDEN;
 //       public static final int ABGLEICH_SCHULDEN = 3;
 //       public static final int �BERNAHME_SCHULDEN = 4;
    };
 


}
