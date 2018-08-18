package org.nill.abrechnung.repositories;


import org.nill.abrechnung.aufzählungen.ParameterKey;
import org.nill.abrechnung.entities.Parameter;
import org.nill.abrechnung.interfaces.IParameterRepository;
import org.nill.allgemein.values.MonatJahr;
import org.nill.allgemein.values.TypeReference;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ParameterRepository extends CrudRepository<Parameter, Long>, IParameterRepository {

    @Override
    @Query("select max(p.mj) from org.nill.abrechnung.entities.Parameter p where p.key = :key and p.ref = :ref and p.mj <= :mj")
    public MonatJahr getMJ(@Param("key") ParameterKey key,
            @Param("ref") TypeReference ref, @Param("mj") MonatJahr mj);

    @Override
    @Query("select p.wert from org.nill.abrechnung.entities.Parameter p where p.key = :key and p.ref = :ref and p.mj is null")
    public String getWert(@Param("key") ParameterKey key,
            @Param("ref") TypeReference ref);

    @Override
    @Query("select p.wert from org.nill.abrechnung.entities.Parameter p where p.key = :key and p.ref = :ref and p.mj = :mj")
    public String getWert(@Param("key") ParameterKey key,
            @Param("ref") TypeReference ref, @Param("mj") MonatJahr mj);


    
    @Override
    public default String getZeitWert(ParameterKey key, TypeReference ref,
            MonatJahr akuellesMj) {
        try {
            MonatJahr mj = getMJ(key, ref, akuellesMj);
            if (mj == null) {
                return null;
            }
            return getWert(key, ref, mj);
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    };

    @Override
    public default Double getDoubleZeitWert(ParameterKey key,
            TypeReference ref, MonatJahr akuellesMj) {
        String sWert = getZeitWert(key, ref, akuellesMj);
        return (sWert == null) ? null : Double.parseDouble(sWert);
    }


    @Override
    public default Integer getIntZeitWert(ParameterKey key,
            TypeReference ref, MonatJahr akuellesMj) {
        String sWert = getZeitWert(key, ref, akuellesMj);
        return (sWert == null) ? null : Integer.parseInt(sWert);
    }

}
