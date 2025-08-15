package com.jobby.authorization.domain.ports.out.encrypt;

import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.infraestructure.config.EncryptConfig;

public interface EncryptionService {
    Result<String, Error> encrypt(String data, EncryptConfig config);
    Result<String, Error> decrypt(String cipherText, EncryptConfig config);
}
