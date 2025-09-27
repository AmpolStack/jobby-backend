package com.jobby.domain.ports.hashing;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface HashingService {
    Result<String, Error> hash(String input);
    Result<Boolean, Error> matches(String plain, String hash);
}
