package com.jobby.business.domain.ports.out.repositories;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface WriteOnlyBusinessRepository {
    Result<Business, Error> save(Business business);
    Result<Business, Error> update(Business business);
    Result<Business, Error> findById(int id);
    Result<Void, Error> delete(int id);
    
    // Nuevos métodos específicos para updates optimizados
    Result<Business, Error> updatePictures(int id, String bannerImageUrl, String profileImageUrl);
    Result<Business, Error> updateName(int id, String name);
    Result<Business, Error> updateDescription(int id, String description);
    Result<Business, Error> updateNameAndDescription(int id, String name, String description);
}