package org.example.poketrade.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdateListingRequest(
        @Positive
        BigDecimal price,

        @Size(max = 1000)
        String description
) {

}
