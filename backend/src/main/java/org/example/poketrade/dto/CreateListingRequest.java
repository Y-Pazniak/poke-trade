package org.example.poketrade.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateListingRequest(
        @NotNull
        Long pokemonId,
        @Positive
        BigDecimal price) {

}
