package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.result.Error;
import com.jobby.authorization.domain.result.ErrorType;
import com.jobby.authorization.domain.result.Field;
import com.jobby.authorization.domain.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtGeneratorService implements TokenGeneratorService {

    private final static int[] VALID_SECRET_KEY_LENGTHS_BITS = { 256, 384, 512 };

    private Result<Void, Error> validateTokenData(TokenData data){

        if(data == null){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("tokenData",
                            "The provided token data is null")
            );
        }

        if(data.getEmail() == null){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("tokenData.email",
                            "The email in token data is null")
            );
        }

        if(data.getPhone() == null){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("tokenData.phone",
                            "The phone in token data is null")
            );
        }

        return Result.success(null);
    }

    private Result<SecretKey, Error> validateAndParseKey(String key){
        if(key == null || key.isBlank()){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("key",
                            "The token key is null or empty")
            );
        }

        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(key);
        } catch (IllegalArgumentException e) {
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("key", "The key is not valid base64"));
        }

        var keyLengthBits = keyBytes.length * 8;
        if(Arrays.stream(VALID_SECRET_KEY_LENGTHS_BITS).noneMatch(x -> x == keyLengthBits)){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("key",
                            "The key length are invalid")
            );
        }

        var keyParsed = Keys.hmacShaKeyFor(keyBytes);
        return Result.success(keyParsed);
    }

    private Result<Void, Error> validateExpirationTime(int expirationTime){
        if(expirationTime <= 0){
            return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                    new Field("expirationTime",
                            "The expiration time are invalid")
            );
        }

        return Result.success(null);
    }

    @Override
    public Result<String, Error> generateToken(TokenData data, String key, int expirationTime) {
        return validateTokenData(data)
                .flatMap(x -> validateExpirationTime(expirationTime))
                .flatMap(x -> validateAndParseKey(key))
                .map(secretKey ->
                    Jwts.builder()
                            .subject(Integer.toString(data.getId()))
                            .claim("email", data.getEmail())
                            .claim("phone", data.getPhone())
                            .issuedAt(new Date())
                            .expiration(new Date(new Date().getTime() + expirationTime))
                            .signWith(secretKey)
                            .compact()
                );
    }


}
