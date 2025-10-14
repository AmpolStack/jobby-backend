package com.jobby.business.application.useCase;

import com.jobby.business.domain.entities.Business;
import com.jobby.business.domain.ports.in.FindBusinessByIdQuery;
import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import org.springframework.stereotype.Service;

@Service
public class FindBusinessByIdQueryImpl implements FindBusinessByIdQuery {

    private final ReadOnlyBusinessRepository businessRepository;

    public FindBusinessByIdQueryImpl(ReadOnlyBusinessRepository businessRepository) {
        this.businessRepository = businessRepository;
    }

    @Override
    public Result<Business, Error> execute(int id) {
        return this.businessRepository.findById(id);
    }
}
