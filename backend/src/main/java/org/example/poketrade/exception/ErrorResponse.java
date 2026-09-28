package org.example.poketrade.exception;

import java.time.OffsetDateTime;

public record ErrorResponse(ErrorCode code, String message, OffsetDateTime timestamp) {

    public ErrorResponse(ErrorCode code, String message) {
        this(code, message, OffsetDateTime.now());
    }
}
