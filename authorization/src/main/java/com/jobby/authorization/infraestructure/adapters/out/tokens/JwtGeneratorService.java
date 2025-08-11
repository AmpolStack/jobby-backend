package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.authorization.domain.shared.validators.NumberValidator;
import com.jobby.authorization.domain.shared.validators.ObjectValidator;
import com.jobby.authorization.domain.shared.validators.StringValidator;
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

    private final static Integer[] VALID_SECRET_KEY_LENGTHS_BITS = { 256, 384, 512 };
    private final static String EMAIL_CLAIM_NAME = "com.jobby.employee.email";

    private Result<Void, Error> validateTokenData(TokenData data){
        return ObjectValidator.validateNotNullObject(data, "data")
                .flatMap(v -> ObjectValidator.validateNotNullObject(data.getIssuer(), "data.issuer"))
                .flatMap(v -> ObjectValidator.validateNotNullObject(data.getAudience(), "data.audience"))
                .flatMap(v -> ObjectValidator.validateNotNullObject(data.getEmail(), "data.email"))
                .flatMap(v -> NumberValidator.validateGreaterNotEqualsLong(data.getMsExpirationTime(), 0, "data.ms-expiration-time"));
    }

    private Result<SecretKey, Error> validateAndParseKey(String base64Key){
        return StringValidator.validateNotBlankString(base64Key, "jwt-key")
                .flatMap(v ->{
                    byte[] keyBytes;
                    try {
                        keyBytes = Base64.getDecoder().decode(base64Key);
                    } catch (IllegalArgumentException e) {
                        return Result.failure(ErrorType.ITN_INVALID_OPTION_PARAMETER,
                                new Field("key", "The key is not valid base64"));
                    }

                    final int BITS_MULTIPLIER = 8;
                    var keyLengthBits = keyBytes.length * BITS_MULTIPLIER;
                    return ObjectValidator.validateAnyMatch(keyLengthBits, VALID_SECRET_KEY_LENGTHS_BITS, "jwt-key-length")
                            .map(v2 -> Keys.hmacShaKeyFor(keyBytes));
                });
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
        return StringValidator.validateNotBlankString(token, "token")
                .flatMap(x -> validateAndParseKey(base64Key))
                .flatMap(secretKey -> parseClaims(secretKey, token))
                .flatMap(this::mapClaimsToTokenData);
    }

    @Override
    public Result<Boolean, Error> isValid(String token, String base64Key) {
        return StringValidator.validateNotBlankString(token, "token")
                .flatMap(x -> validateAndParseKey(base64Key))
                .flatMap(secretKey -> parseClaims(secretKey, token))
                .map(claims -> claims.getExpiration().after(new Date()));
    }
}
