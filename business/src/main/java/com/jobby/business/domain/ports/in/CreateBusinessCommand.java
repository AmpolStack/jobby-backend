package com.jobby.business.domain.ports.in;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface CreateBusinessCommand {
    Result<Business, Error> execute(Business business);
}
