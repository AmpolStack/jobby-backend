package com.jobby.business.domain.operations;

import com.jobby.business.domain.ports.out.repositories.ReadOnlyBusinessRepository;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public abstract class BusinessSaveEvent {
    public abstract Result<Void, Error> execute(ReadOnlyBusinessRepository readBusinessRepository);
}
