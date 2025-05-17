package org.unibuc.chirp.domain.exception;

import lombok.Getter;
import lombok.NonNull;

@Getter
public enum ErrorCode {
    CHR0001("Username already taken"),
    CHR0002("User not found"),
    CHR0003("Bio cannot be null"),
    CHR0004("User profile not found"),
    CHR0005("Conversation title cannot be empty"),
    CHR0006("Conversation title too long"),;

    private final String message;

    ErrorCode(@NonNull final String message) {
        this.message = message;
    }
}
