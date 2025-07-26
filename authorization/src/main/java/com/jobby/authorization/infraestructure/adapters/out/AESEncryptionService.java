package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.EncryptionService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

@Service
public class AESEncryptionService implements EncryptionService {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";

    private final EncryptGenerators encryptGenerators;

    public AESEncryptionService(EncryptGenerators encryptGenerators) {
        this.encryptGenerators = encryptGenerators;
    }

    @Override
    public Result<String, Error> encrypt(String data, String keyBase64, int ivLength) {

        if(keyBase64.isEmpty()) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Encryption key cannot be empty"
                    )
            );
        }

        if(ivLength <= 0 || ivLength > 16){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "ivLength",
                            "IV length must be between 1 and 16 bytes"
                    )
            );
        }

        var key = this.encryptGenerators.ParseKeySpec(ALGORITHM, keyBase64);

        if(key == null){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Invalid Base64 encoded key or incompatible key format"
                    )
            );
        }


        if (key.getEncoded().length < 16 || key.getEncoded().length > 32) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Key length must be between 16 and 32 bytes for AES encryption"
                    )
            );
        }

        var iv = this.encryptGenerators.generateIv(key.getEncoded().length * 8, ivLength);
        var executionResult = this.encryptGenerators.runAlgorithm(
                data.getBytes(StandardCharsets.UTF_8),
                key, iv,
                Cipher.ENCRYPT_MODE, TRANSFORMATION
        );

        if(executionResult.isFailure()){
            return Result.renewFailure(executionResult);
        }

        var cipherBytes = executionResult.getData();
        var buffer = ByteBuffer.allocate(iv.getIV().length + cipherBytes.length);
        buffer.put(iv.getIV());
        buffer.put(cipherBytes);
        var combined = buffer.array();

        var response = Base64.getEncoder().encodeToString(combined);
        return Result.success(response);
    }

    @Override
    public Result<String, Error> decrypt(String cipherText, String keyBase64, int ivLength) {

        if(keyBase64.isEmpty()) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Decryption key cannot be empty"
                    )
            );
        }

        if(cipherText.isEmpty()){
            return Result.failure(ErrorType.INVALID_INPUT,
                    new Field(
                            "cipherText",
                            "Cipher text cannot be empty"
                    )
            );
        }

        byte[] combined;
        try{
            combined = Base64.getDecoder().decode(cipherText);
        }
        catch (IllegalArgumentException e){
            return Result.failure(ErrorType.INVALID_INPUT,
                    new Field(
                            "cipherText",
                            "Invalid Base64 encoded cipher text"
                    )
            );
        }

        if(ivLength > combined.length){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field(
                            "cipherText",
                            "Cipher text is too short to contain valid data (expected at least " + ivLength + " bytes)"
                    )
            );
        }

        var key = this.encryptGenerators.ParseKeySpec(ALGORITHM, keyBase64);

        if(key == null){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Invalid Base64 encoded key or incompatible key format"
                    )
            );
        }

        if (key.getEncoded().length < 16 || key.getEncoded().length > 32) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "keyBase64",
                            "Key length must be between 16 and 32 bytes for AES decryption"
                    )
            );
        }

        var rawIv = Arrays.copyOfRange(combined, 0, ivLength);
        var rawData = Arrays.copyOfRange(combined, ivLength, combined.length);

        var iv = new GCMParameterSpec(key.getEncoded().length * 8, rawIv);

        var executionResult = this.encryptGenerators.runAlgorithm(
                rawData,
                key,
                iv,
                Cipher.DECRYPT_MODE,
                TRANSFORMATION
        );

        if(executionResult.isFailure()){
            return Result.renewFailure(executionResult);
        }

        var resp = new String(executionResult.getData(), StandardCharsets.UTF_8);
        return Result.success(resp);
    }
}
