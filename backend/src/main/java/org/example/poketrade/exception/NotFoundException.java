package org.example.poketrade.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException listing(Long id) {
        return new NotFoundException("Listing not found: " + id);
    }

    public static NotFoundException trainer(Long id) {
        return new NotFoundException("Trainer not found: " + id);
    }

    public static NotFoundException pokemon(Long id) {
        return new NotFoundException("Pokemon not found: " + id);
    }
}
