package boundingContext.abrechnung.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundingContext.abrechnung.entities.Abrechnung;
import boundingContext.abrechnung.entities.ZahlungsAuftrag;

public interface ZahlungsAuftragRepository extends
        CrudRepository<ZahlungsAuftrag, Long> {

    @Query("select z from boundingContext.abrechnung.entities.ZahlungsAuftrag z where z.abrechnung = :abr and z.buchungsart = :art and z.ausbezahlt is null")
    public List<ZahlungsAuftrag> getOffeneZahlungen(
            @Param("abr") Abrechnung abrechnung, @Param("art") int art);

}
