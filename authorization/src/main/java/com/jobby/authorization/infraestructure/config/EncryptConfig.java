package com.jobby.authorization.infraestructure.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.annotation.Validated;

@Validated
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EncryptConfig {
    @NotNull(message = "encrypt.secret-key.value is required")
    @NotEmpty(message = "encrypt.secret-key.value must not be empty")
    private String secretKey;

    @Valid
    @NotNull(message = "encrypt.secret-key is required")
    private Iv iv = new Iv();

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Iv {
        @NotNull(message = "encrypt.iv.length is required")
        @Min(value = 8, message = "encrypt.iv.length must be at least 8")
        @Max(value = 16, message = "encrypt.iv.length must be at most 16")
        private int length;
        @NotNull(message = "encrypt.iv.length is required")
        @Min(value = 98, message = "encrypt.iv.t-len must be at least 98")
        @Max(value = 128, message = "encrypt.iv.t-len must be at most 128")
        private int tLen;
    }
}
