package org.example.poketrade.builder;

import org.example.poketrade.TestConstants;
import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.dto.PokemonResponse;
import org.example.poketrade.dto.TrainerResponse;
import org.example.poketrade.entity.Listing;
import org.example.poketrade.entity.Pokemon;
import org.example.poketrade.entity.Trainer;

public class TestBuilder {

    public static Listing createListing() {
        Trainer trainer = createTrainer();
        Pokemon pokemon = createPokemon(trainer);
        return Listing.create(
                trainer,
                pokemon,
                TestConstants.LISTING_PRICE,
                TestConstants.LISTING_DESCRIPTION
        );
    }

    public static ListingResponse createListingResponse() {
        return new ListingResponse(
                TestConstants.LISTING_ID,
                createTrainerResponse(),
                createPokemonResponse(),
                TestConstants.LISTING_PRICE,
                TestConstants.LISTING_DESCRIPTION,
                TestConstants.LISTING_DATE
        );
    }

    private static Trainer createTrainer() {
        return Trainer.create(
                TestConstants.TRAINER_NAME,
                TestConstants.TRAINER_EMAIL
        );
    }

    private static Pokemon createPokemon(Trainer trainer) {
        return Pokemon.create(
                TestConstants.POKEMON_SPECIES,
                TestConstants.POKEMON_LEVEL,
                trainer
        );
    }

    private static TrainerResponse createTrainerResponse() {
        return new TrainerResponse(
                TestConstants.TRAINER_ID,
                TestConstants.TRAINER_NAME);
    }

    private static PokemonResponse createPokemonResponse() {
        return new PokemonResponse(
                TestConstants.POKEMON_ID,
                TestConstants.POKEMON_SPECIES,
                TestConstants.POKEMON_LEVEL
        );
    }
}
