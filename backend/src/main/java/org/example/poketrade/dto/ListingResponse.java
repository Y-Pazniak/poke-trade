package org.example.poketrade.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record ListingResponse(
        Long id,
        TrainerResponse seller,
        PokemonResponse pokemon,
        BigDecimal price,
        OffsetDateTime createdAt) {

}
