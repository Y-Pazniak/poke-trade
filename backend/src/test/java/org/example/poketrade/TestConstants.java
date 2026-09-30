package org.example.poketrade;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class TestConstants {

    public static final Long LISTING_ID = 1L;
    public static final BigDecimal LISTING_PRICE = new BigDecimal("100.00");
    public static final String LISTING_DESCRIPTION = "listing description";
    public static final OffsetDateTime LISTING_DATE = OffsetDateTime.of(2026, 1, 1, 12, 0, 0, 0, ZoneOffset.UTC);

    public static final Long TRAINER_ID = 1L;
    public static final String TRAINER_NAME = "Ash";
    public static final String TRAINER_EMAIL = "ketchum@gmail.com";

    public static final Long POKEMON_ID = 1L;
    public static final String POKEMON_SPECIES = "pikachuh";
    public static final Integer POKEMON_LEVEL = 10;

}
