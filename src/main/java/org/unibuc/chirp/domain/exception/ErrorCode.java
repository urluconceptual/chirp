package org.unibuc.chirp.domain.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum ErrorCode {
    CHR0001("Username already taken");

    private final String message;

    ErrorCode(@NonNull final String message) {
        this.message = message;
    }
}
