package com.jobby.domain.ports;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface MacService {
    Result<String, Error> generateMac(String data, MacConfig config);
    Result<Boolean, Error> verifyMac(String data, String mac, MacConfig config);
}
