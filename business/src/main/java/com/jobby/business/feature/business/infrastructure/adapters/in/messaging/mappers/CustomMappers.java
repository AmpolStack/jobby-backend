package com.jobby.business.feature.business.infrastructure.adapters.in.messaging.mappers;

import org.springframework.stereotype.Component;

@Component
public class CustomMappers {
    public String fromCharSequence(CharSequence value) {
        return value == null ? null : value.toString();
    }

    public CharSequence toCharSequence(String value) {
        return value;
    }

    public long map(java.time.Instant value) {
        return value == null ? 0L : value.toEpochMilli();
    }

    public java.time.Instant map(long value) {
        return java.time.Instant.ofEpochMilli(value);
    }

    public String map(byte[] value) {
        return value == null ? null : new String(value, java.nio.charset.StandardCharsets.UTF_8);
    }

    public byte[] map(String value) {
        return value == null ? null : value.getBytes(java.nio.charset.StandardCharsets.UTF_8);
    }

}
