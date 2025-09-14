package com.jobby.domain.ports.encrypt;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

import java.security.Key;
import java.security.spec.AlgorithmParameterSpec;

public interface EncryptBuilder {
    EncryptBuilder setData(byte[] data);
    EncryptBuilder setKey(Key key);
    EncryptBuilder setIv(AlgorithmParameterSpec iv);
    EncryptBuilder setMode(int mode);
    EncryptBuilder setTransformation(String transformation);
    Result<byte[], Error> build();
}
