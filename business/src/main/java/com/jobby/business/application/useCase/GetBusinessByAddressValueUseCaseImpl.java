package com.jobby.business.application.useCase;

import com.jobby.business.domain.Business;
import com.jobby.business.domain.ports.in.GetBusinessByAddressValueUseCase;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.domain.ports.hashing.mac.MacService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GetBusinessByAddressValueUseCaseImpl implements GetBusinessByAddressValueUseCase {

    private final BusinessRepository businessRepository;
    private final MacService macService;
    private final MacConfig macConfig;

    public GetBusinessByAddressValueUseCaseImpl(@Qualifier("read") BusinessRepository businessRepository, MacService macService, MacConfig macConfig) {
        this.businessRepository = businessRepository;
        this.macService = macService;
        this.macConfig = macConfig;
    }

    @Override
    public Result<Business, Error> execute(String value) {
        return this.macService.generateMac(value, this.macConfig)
            .flatMap(this.businessRepository::findByAddress_ValueSearchable);
    }
}
