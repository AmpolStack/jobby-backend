package com.jobby.authorization.infraestructure.adapters.out.tokens;

import com.jobby.authorization.domain.ports.out.SafeResultValidator;
import com.jobby.authorization.domain.ports.out.tokens.TokenGeneratorService;
import com.jobby.domain.mobility.Error;
import com.jobby.domain.mobility.ErrorType;
import com.jobby.domain.mobility.Field;
import com.jobby.authorization.domain.shared.result.Result;
import com.jobby.authorization.domain.shared.TokenData;
import com.jobby.authorization.domain.shared.validators.ValidationChain;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.util.*;

@Service
public class JwtGeneratorService implements TokenGeneratorService {

    private final static Integer[] VALID_SECRET_KEY_LENGTHS_BITS = { 256, 384, 512 };
    private final static String EMAIL_CLAIM_NAME = "com.jobby.employee.email";
    private final SafeResultValidator validator;

    public JwtGeneratorService(SafeResultValidator validator) {
        this.validator = validator;
    }


    private Result<SecretKey, Error> validateAndParseKey(String base64Key){
        return ValidationChain.create()
                .validateInternalNotBlank(base64Key, "jwt-key")
                .build()
                .flatMap(v ->{
                    byte[] keyBytes;
                    try {
                        keyBytes = Base64.getDecoder().decode(base64Key);
                    } catch (IllegalArgumentException e) {
                        return Result.failure(ErrorType.ITS_INVALID_OPTION_PARAMETER,
                                new Field("key", "The key is not valid base64"));
                    }

                    final int BITS_MULTIPLIER = 8;
                    var keyLengthBits = keyBytes.length * BITS_MULTIPLIER;
                    return ValidationChain.create()
                            .validateInternalAnyMatch(keyLengthBits, VALID_SECRET_KEY_LENGTHS_BITS, "jwt-length")
                            .build()
                            .map(v2 -> Keys.hmacShaKeyFor(keyBytes));
                });
    }

    @Override
    public Result<String, Error> generate(TokenData data, String base64Key) {
        return this.validator.validate(data)
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
            return Result.failure(ErrorType.ITS_OPERATION_ERROR,
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
            return Result.failure(ErrorType.ITS_OPERATION_ERROR,
                    new Field("jwt", "The provided token is invalid")
            );
        }
        return Result.success(claims.getPayload());
    }

    @Override
    public Result<TokenData, Error> obtainData(String token, String base64Key) {
        return ValidationChain.create()
                .validateInternalNotBlank(token, "jwt")
                .build()
                .flatMap(x -> validateAndParseKey(base64Key))
                .flatMap(secretKey -> parseClaims(secretKey, token))
                .flatMap(this::mapClaimsToTokenData);
    }

    @Override
    public Result<Boolean, Error> isValid(String token, String base64Key) {
        return ValidationChain.create()
                .validateInternalNotBlank(token, "jwt")
                .build()
                .flatMap(x -> validateAndParseKey(base64Key))
                .flatMap(secretKey -> parseClaims(secretKey, token))
                .map(claims -> claims.getExpiration().after(new Date()));
    }
}
