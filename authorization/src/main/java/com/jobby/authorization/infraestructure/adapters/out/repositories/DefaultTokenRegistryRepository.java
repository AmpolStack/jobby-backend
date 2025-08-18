package com.jobby.authorization.infraestructure.adapters.out.repositories;

import com.jobby.authorization.domain.ports.out.CacheService;
import com.jobby.authorization.domain.model.TokenRegistry;
import com.jobby.authorization.domain.ports.out.repositories.TokenRegistryRepository;
import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.errors.ErrorType;
import com.jobby.authorization.domain.shared.errors.Field;
import com.jobby.authorization.domain.shared.result.Result;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class DefaultTokenRegistryRepository implements TokenRegistryRepository {

    private final CacheService cacheService;

    public DefaultTokenRegistryRepository(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Override
    public Result<TokenRegistry, Error> getTokenRegistry(String token, String refreshToken, int id) {
        var key = generateKey(token, refreshToken, id);
        return this.cacheService.get(key, TokenRegistry.class)
                .flatMap(registry ->{
                    if(registry == null){
                        return Result.failure(ErrorType.NOT_FOUND,
                                new Field("token-registry", "The token registry is null"));
                    }
                    return Result.success(registry);
                });
    }

    @Override
    public Result<Void, Error> saveTokenRegistry(TokenRegistry tokenRegistry) {
        var key = generateKey(tokenRegistry.getToken(), tokenRegistry.getRefreshToken(), tokenRegistry.getId());
        return this.cacheService.put(key, tokenRegistry, Duration.ofMinutes(2));
    }

    private String generateKey(String token, String refreshToken, int id) {
        return id + token + refreshToken;
    }

    @Override
    public Result<Void, Error> deleteTokenRegistry(String token, String refreshToken, int id) {
        var key = generateKey(token, refreshToken, id);
        return this.cacheService.evict(key);
    }
}
