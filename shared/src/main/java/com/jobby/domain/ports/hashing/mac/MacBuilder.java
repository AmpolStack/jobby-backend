package com.jobby.domain.ports.hashing.mac;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;
import com.jobby.infraestructure.adapter.hashing.mac.DefaultMacBuilder;
import java.security.Key;

public interface MacBuilder {
    DefaultMacBuilder setData(byte[] data);
    DefaultMacBuilder setKey(Key key);
    DefaultMacBuilder setAlgorithm(String algorithm);
    Result<byte[], Error> build();
}
