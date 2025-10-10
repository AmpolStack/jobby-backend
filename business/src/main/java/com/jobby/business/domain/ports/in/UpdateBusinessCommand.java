package com.jobby.business.domain.ports.in;

import com.jobby.business.domain.entities.Business;
import com.jobby.domain.mobility.result.Result;

public interface UpdateBusinessCommand {
    public Result<Business, Error> update(Business business);
}
