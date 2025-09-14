package com.jobby.domain.ports.encrypt;

import com.jobby.domain.configurations.EncryptConfig;
import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.result.Result;

public interface EncryptionService {
    Result<String, Error> encrypt(String data, EncryptConfig config);
    Result<String, Error> decrypt(String cipherText, EncryptConfig config);
}
