package com.jobby.authorization.domain.ports.out.repositories;

import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface TokenRegistryRepository {
    Result<TokenRegistry, Error> getTokenRegistry(String token, String refreshToken, int id);
    Result<Void, Error> saveTokenRegistry(TokenRegistry tokenRegistry);
    Result<Void, Error> deleteTokenRegistry(String token, String refreshToken, int id);
}
