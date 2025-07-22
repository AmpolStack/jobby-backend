package com.jobby.authorization.application.ports.out;

public interface HashingService {
    String hash(String input);
    boolean matches(String input, String hash);
}
