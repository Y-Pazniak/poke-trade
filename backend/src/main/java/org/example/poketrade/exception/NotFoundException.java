package org.example.poketrade.exception;

public class NotFoundException extends RuntimeException {

    public static final String LISTING_NOT_FOUND_FORMAT = "Listing not found: %s";
    public static final String TRAINER_NOT_FOUND_FORMAT = "Trainer not found: %s";
    public static final String POKEMON_NOT_FOUND_FORMAT = "Pokemon not found: %s";

    public NotFoundException(String message) {
        super(message);
    }

    public static NotFoundException listing(Long id) {
        return new NotFoundException(LISTING_NOT_FOUND_FORMAT.formatted(id));
    }

    public static NotFoundException trainer(Long id) {
        return new NotFoundException(TRAINER_NOT_FOUND_FORMAT.formatted(id));
    }

    public static NotFoundException pokemon(Long id) {
        return new NotFoundException(POKEMON_NOT_FOUND_FORMAT.formatted(id));
    }
}
