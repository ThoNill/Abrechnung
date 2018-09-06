package org.nill.abrechnung.interfaces;

public interface IMandantRepository {

    IMandant save(IMandant mandant);

    IMandant findOne(long mandantId);

}