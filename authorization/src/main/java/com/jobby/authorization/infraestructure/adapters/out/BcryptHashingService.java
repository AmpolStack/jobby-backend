package com.jobby.authorization.infraestructure.adapters.out;

import com.jobby.authorization.application.ports.out.HashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BcryptHashingService implements HashingService {
    @Override
    public String hash(String input) {
        var encoder = new BCryptPasswordEncoder();
        return encoder.encode(input);
    }

    @Override
    public boolean matches(String plain, String hash) {
        var encoder = new BCryptPasswordEncoder();
        return encoder.matches(plain, hash);
    }
}
