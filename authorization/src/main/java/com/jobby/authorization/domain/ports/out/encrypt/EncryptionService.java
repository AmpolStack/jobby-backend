package com.jobby.authorization.domain.ports.out.encrypt;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.infraestructure.config.EncryptConfig;

public interface EncryptionService {
    public Result<String, Error> encrypt(String data, EncryptConfig config);
    public Result<String, Error> decrypt(String cipherText, EncryptConfig config);
}
