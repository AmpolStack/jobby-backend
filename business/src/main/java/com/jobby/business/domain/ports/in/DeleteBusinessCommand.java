package com.jobby.business.domain.ports.in;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface DeleteBusinessCommand {
    Result<Void, Error> delete(int id);
}
