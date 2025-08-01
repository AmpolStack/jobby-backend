package com.jobby.authorization.application.ports.out.encrypt;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.adapters.out.encrypt.DefaultEncryptBuilder;

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
