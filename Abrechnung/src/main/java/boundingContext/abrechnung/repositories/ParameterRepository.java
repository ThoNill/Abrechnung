package boundingContext.abrechnung.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import boundingContext.abrechnung.aufzählungen.ParameterKey;
import boundingContext.abrechnung.entities.Parameter;
import boundingContext.zahlungen.values.MonatJahr;
import boundingContext.zahlungen.values.TypeReference;

public interface ParameterRepository extends CrudRepository<Parameter, Long> {

    @Query("select max(p.mj) from boundingContext.abrechnung.entities.Parameter p where p.key = :key and p.ref = :ref and p.mj <= :mj")
    public MonatJahr getMJ(@Param("key") ParameterKey key,
            @Param("ref") TypeReference ref, @Param("mj") MonatJahr mj);

    @Query("select p.wert from boundingContext.abrechnung.entities.Parameter p where p.key = :key and p.ref = :ref and p.mj is null")
    public String getWert(@Param("key") ParameterKey key,
            @Param("ref") TypeReference ref);

    @Query("select p.wert from boundingContext.abrechnung.entities.Parameter p where p.key = :key and p.ref = :ref and p.mj = :mj")
    public String getWert(@Param("key") ParameterKey key,
            @Param("ref") TypeReference ref, @Param("mj") MonatJahr mj);

    public default String getZeitWert(ParameterKey key, TypeReference ref,
            MonatJahr akuellesMj) {
        try {
            MonatJahr mj = getMJ(key, ref, akuellesMj);
            if (mj == null) {
                return null;
            }
            return getWert(key, ref, mj);
        } catch (Exception ex) {
            return null;
        }
    };

    public default Double getDoubleZeitWert(ParameterKey key,
            TypeReference ref, MonatJahr akuellesMj) {
        String sWert = getZeitWert(key, ref, akuellesMj);
        return (sWert == null) ? null : Double.parseDouble(sWert);
    }

}
