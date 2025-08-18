package com.jobby.authorization.domain.ports.out.repositories;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface TokenRegistryRepository {
    Result<TokenRegistry, Error> getTokenRegistry(String token, String refreshToken, int id);
    Result<Void, Error> saveTokenRegistry(TokenRegistry tokenRegistry);
    Result<Void, Error> deleteTokenRegistry(String token, String refreshToken, int id);
}
