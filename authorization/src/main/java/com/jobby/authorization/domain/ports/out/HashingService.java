package com.jobby.authorization.domain.ports.out;

import com.jobby.domain.mobility.Error;
import com.jobby.authorization.domain.shared.result.Result;

public interface HashingService {
    Result<String, Error> hash(String input);
    Result<Boolean, Error> matches(String plain, String hash);
}
