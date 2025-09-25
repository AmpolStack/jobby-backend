package com.jobby.infraestructure.adapter.encrypt;

import com.jobby.domain.mobility.error.Error;
import com.jobby.domain.mobility.error.ErrorType;
import com.jobby.domain.mobility.error.Field;
import com.jobby.domain.mobility.result.Result;

import javax.crypto.Mac;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

public class DefaultMacBuilder {
    private byte[] data;
    private Key key;
    private String algorithm;
    
    public DefaultMacBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    public DefaultMacBuilder setKey(Key key) {
        this.key = key;
        return this;
    }

    public DefaultMacBuilder setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
        return this;
    }

    public Result<byte[], Error> build() {
        Mac mac;
        try {
            mac = Mac.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            return Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                    new Field(
                            "algorithm",
                            "Invalid MAC algorithm: " + algorithm
                    )
            );
        }

        try {
            mac.init(this.key);
        } catch (InvalidKeyException e) {
            return Result.failure(ErrorType.ITS_OPERATION_ERROR,
                    new Field(
                            "key",
                            "Invalid key for MAC: " + key
                    )
            );
        }

        try {
            byte[] macBytes = mac.doFinal(this.data);
            return Result.success(macBytes);
        } catch (IllegalStateException e) {
            return Result.failure(ErrorType.ITS_OPERATION_ERROR,
                    new Field(
                            "data",
                            "MAC operation failed - data may be corrupted"
                    )
            );
        }
    }
}
