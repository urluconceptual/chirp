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
    CHR0006("Conversation title too long"),
    CHR0007("Conversation ID not found"),
    CHR0008("Invalid username or password"),
    CHR0009("Message content cannot be null or empty"),
    CHR0010("Friendship is not in PENDING status"),
    CHR0011("Friendship request must have both requester and addressee set"),
    CHR0012("Friendship is not is ACCEPTED status"),;

    private final String message;

    ErrorCode(@NonNull final String message) {
        this.message = message;
    }
}
