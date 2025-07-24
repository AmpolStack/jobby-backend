package com.jobby.authorization.application.ports.out;

import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.Result;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface EncryptionService {
    public Result<String, Error> encrypt(String plainText);
    public Result<String, Error> decrypt(String cipherText) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException;
}
