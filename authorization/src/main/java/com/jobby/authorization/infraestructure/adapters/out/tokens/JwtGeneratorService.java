package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.shared.result.Error;
import com.jobby.authorization.domain.shared.result.ErrorType;
import com.jobby.authorization.domain.shared.result.Field;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtGeneratorService implements TokenGeneratorService {

    private final static int[] VALID_SECRET_KEY_LENGTHS_BITS = { 256, 384, 512 };
    private final static String EMAIL_CLAIM_NAME = "com.jobby.employee.email";

    private Result<Void, Error> validateTokenData(TokenData data){

        if(data == null){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("tokenData",
                            "The provided token data is null")
            );
        }

        if(data.getIssuer() == null){
            Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("tokenData.issuer",
                            "The provided issuer data is null")
            );
        }

        if(data.getAudience() == null){
            Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("tokenData.audience",
                            "The provided audience data is null")
            );
        }


        if(data.getEmail() == null){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("tokenData.email",
                            "The email in token data is null")
            );
        }

        if(data.getMsExpirationTime() <= 0){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("tokenData.msExpirationTime",
                            "The Expiration time in token data is less than 0")
            );
        }

        return Result.success(null);
    }

    private Result<SecretKey, Error> validateAndParseKey(String base64Key){
        if(base64Key == null || base64Key.isBlank()){
            return Result.failure(ErrorType.VALIDATION_ERROR,
                    new Field("key",
                            "The token key is null or empty")
            );
        }

        byte[] keyBytes;
        try {
            keyBytes = Base64.getDecoder().decode(base64Key);
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

    @Override
    public Result<String, Error> generate(TokenData data, String base64Key) {
        return validateTokenData(data)
                .flatMap(x -> validateAndParseKey(base64Key))
                .map(secretKey ->
                    Jwts.builder()
                            .subject(Integer.toString(data.getId()))
                            .issuer(data.getIssuer())
                            .audience()
                            .add(data.getAudience())
                            .and()
                            .claim(EMAIL_CLAIM_NAME, data.getEmail())
                            .issuedAt(new Date())
                            .expiration(new Date(new Date().getTime() + data.getMsExpirationTime()))
                            .signWith(secretKey)
                            .compact()
                );
    }

    private Result<Void, Error> validateToken(String token){
        if(token == null || token.isBlank()){
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("token",
                            "The provided token is null or blank")
            );
        }

        return Result.success(null);
    }

    private Result<TokenData, Error> mapClaimsToTokenData(Claims claims) {
        int id;
        try {
            id = Integer.parseInt(claims.getSubject());
        } catch (NumberFormatException e) {
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("sub", "The subject is not a valid integer"));
        }

        long expiration = claims.getExpiration() != null
                ? claims.getExpiration().getTime()
                : 0L;

        String issuer = claims.getIssuer();
        String audience = claims.getAudience().stream().findFirst().orElse("");
        String email = claims.get(EMAIL_CLAIM_NAME, String.class);

        TokenData tokenData = new TokenData(id, issuer, audience, email, expiration);
        return Result.success(tokenData);
    }


    private Result<Claims, Error> parseClaims(SecretKey secretKey, String token){
        var parser = Jwts.parser()
                .verifyWith(secretKey)
                .build();

        Jws<Claims> claims;
        try{
            claims = parser.parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException e) {
            return Result.failure(ErrorType.ITN_OPERATION_ERROR,
                    new Field("token", "The provided token is invalid")
            );
        }
        return Result.success(claims.getPayload());
    }

    @Override
    public Result<TokenData, Error> obtainData(String token, String base64Key) {
        return validateToken(token)
                .flatMap(x -> validateAndParseKey(base64Key))
                .flatMap(secretKey -> parseClaims(secretKey, token))
                .flatMap(this::mapClaimsToTokenData);
    }

    @Override
    public Result<Boolean, Error> isValid(String token, String base64Key) {
        return validateToken(token)
                .flatMap(x -> validateAndParseKey(base64Key))
                .flatMap(secretKey -> parseClaims(secretKey, token))
                .map(claims -> claims.getExpiration().after(new Date()));
    }
}
