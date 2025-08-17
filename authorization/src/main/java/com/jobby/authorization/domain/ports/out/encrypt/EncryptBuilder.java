package com.jobby.authorization.domain.ports.out.encrypt;

import com.jobby.authorization.domain.shared.errors.Error;
import com.jobby.authorization.domain.shared.result.Result;

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
