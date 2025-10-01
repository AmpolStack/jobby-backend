package com.jobby.domain.ports.hashing.mac;

import com.jobby.domain.configurations.MacConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface MacService {
    Result<byte[], Error> generateMac(String data, MacConfig config);
    Result<Boolean, Error> verifyMac(String data, byte[] mac, MacConfig config);
}
