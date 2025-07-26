package com.jobby.authorization.application.ports.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;

public interface EncryptionService {
    public Result<String, Error> encrypt(String data, String keyBase64, int ivLength);
    public Result<String, Error> decrypt(String cipherText, String keyBase64, int ivLength);
}
