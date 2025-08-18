package com.jobby.authorization.infraestructure.adapters.out.encrypt;

import com.jobby.authorization.domain.ports.out.encrypt.EncryptBuilder;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.mobility.Field;
import com.jobby.authorization.domain.shared.result.Result;
import org.springframework.stereotype.Component;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
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
            return Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                    new Field(
                            "transformation",
                            "Invalid cipher transformation: " + transformation
                    )
            );
        }

        try {
            cipher.init(this.mode, this.key, this.iv);
        }
        catch (InvalidKeyException | InvalidAlgorithmParameterException | InvalidParameterException e){
            return Result.failure(ErrorType.ITS_OPERATION_ERROR,
                    new Field[]{
                            new Field(
                                    "this.key",
                                    "Possible invalid Key: " + key
                            ),
                            new Field(
                                    "this.mode",
                                    "Possible invalid cipher mode: " + mode
                            ),
                            new Field(
                                    "this.Iv",
                                    "Possible invalid cipher iv: " + iv
                            )
                    }
            );
        }

        byte[] cipherBytes;
        try{
            cipherBytes = cipher.doFinal(this.data);
        }
        catch (IllegalBlockSizeException | BadPaddingException e){
            return Result.failure(ErrorType.ITS_OPERATION_ERROR,
                    new Field(
                            "this.data",
                            "Cipher operation failed - data may be corrupted or incompatible"
                    )
            );
        }

        return Result.success(cipherBytes);
    }
}
