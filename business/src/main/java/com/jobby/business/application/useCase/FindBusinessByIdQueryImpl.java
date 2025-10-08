package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.FindBusinessByIdQuery;
import com.jobby.business.domain.ports.out.BusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class FindBusinessByIdQueryImpl implements FindBusinessByIdQuery {

    private final BusinessRepository businessRepository;

    public FindBusinessByIdQueryImpl(@Qualifier("read") BusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Result<Business, Error> execute(int id) {
        return this.businessRepository.findById(id);
    }
}
