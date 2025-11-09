package com.jobby.business.feature.business.infrastructure.internal;

import com.jobby.business.feature.business.domain.entities.Business;
import com.jobby.domain.ports.CacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.time.Duration;

@Component
@Slf4j
public class BusinessCacheFinder {
    private final CacheService cacheService;
    private final static String BUSINESS_CACHE_PREFIX = "business";

    public BusinessCacheFinder(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    private String getKey(int id){
        return BUSINESS_CACHE_PREFIX.concat(":").concat(String.valueOf(id));
    }


    public Business obtainById(int id){
        var cacheResult = this.cacheService.get(getKey(id), Business.class);

        if (cacheResult.isFailure()){
            log.error(cacheResult.getError().toString());
            return null;
        }

        return cacheResult.getData();
    }

    public void register(Business business){
        var id = business.getId();
        var cacheResult = this.cacheService.put(getKey(id), business, Duration.ofMinutes(1));

        if (cacheResult.isFailure()){
            log.error(cacheResult.getError().toString());
        }
    }

    public void deleteRegistry(int id){
        var cacheResult = this.cacheService.evict(getKey(id));

        if (cacheResult.isFailure()){
            log.error(cacheResult.getError().toString());
        }
    }
}
