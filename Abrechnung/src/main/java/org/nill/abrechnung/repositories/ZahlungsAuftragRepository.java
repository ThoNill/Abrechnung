package org.nill.abrechnung.repositories;

import java.util.List;

import org.nill.abrechnung.entities.Abrechnung;
import org.nill.abrechnung.entities.ZahlungsAuftrag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ZahlungsAuftragRepository extends
        CrudRepository<ZahlungsAuftrag, Long> {

    @Query("select z from org.nill.abrechnung.entities.ZahlungsAuftrag z where z.abrechnung = :abr and z.buchungsart = :art and z.ausbezahlt is null")
    public List<ZahlungsAuftrag> getOffeneZahlungen(
            @Param("abr") Abrechnung abrechnung, @Param("art") int art);

}
