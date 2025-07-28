package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.EncryptBuilder;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

@Component
public class DefaultEncryptBuilder implements EncryptBuilder {
    private byte[] data;
    private Key key;
    private AlgorithmParameterSpec iv;
    private int mode;
    private String transformation;
    
    @Override
    public DefaultEncryptBuilder setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public DefaultEncryptBuilder setKey(Key key) {
        this.key = key;
        return this;
    }

    @Override
    public DefaultEncryptBuilder setIv(AlgorithmParameterSpec iv) {
        this.iv = iv;
        return this;
    }

    @Override
    public DefaultEncryptBuilder setMode(int mode) {
        this.mode = mode;
        return this;
    }

    @Override
    public DefaultEncryptBuilder setTransformation(String transformation) {
        this.transformation = transformation;
        return this;
    }

    @Override
    public Result<byte[], Error> build() {
        Cipher cipher;
        try{
            cipher = Cipher.getInstance(transformation);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field(
                            "transformation",
                            "Invalid cipher transformation: " + transformation
                    )
            );
        }

        try {
            cipher.init(this.mode, this.key, this.iv);
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException e){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field[]{
                            new Field(
                                    "this.key",
                                    "Possible invalid Key mode: " + iv
                            ),
                            new Field(
                                    "this.mode",
                                    "Possible invalid cipher mode: " + mode
                            ),
                            new Field(
                                    "this.Iv",
                                    "Possible invalid cipher iv: " + mode
                            )
                    }
            );
        }

        byte[] cipherBytes;
        try{
            cipherBytes = cipher.doFinal(this.data);
        }
        catch (IllegalBlockSizeException | BadPaddingException e){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field(
                            "this.data",
                            "Cipher operation failed - data may be corrupted or incompatible"
                    )
            );
        }

        return Result.success(cipherBytes);
    }
}
