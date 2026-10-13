package org.example.poketrade.builder;

import org.example.poketrade.TestConstants;
import org.example.poketrade.dto.CreateListingRequest;
import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.dto.PokemonResponse;
import org.example.poketrade.dto.TrainerResponse;
import org.example.poketrade.dto.UpdateListingRequest;
import org.example.poketrade.entity.Listing;
import org.example.poketrade.entity.Pokemon;
import org.example.poketrade.entity.Trainer;
import org.springframework.test.util.ReflectionTestUtils;

public class TestBuilder {

    public static Listing createListing() {
        Trainer trainer = createTrainer();
        Pokemon pokemon = createPokemon(trainer);
        Listing listing = Listing.create(trainer,
                pokemon,
                TestConstants.LISTING_PRICE,
                TestConstants.LISTING_DESCRIPTION);
        ReflectionTestUtils.setField(listing, "id", TestConstants.LISTING_ID);
        ReflectionTestUtils.setField(listing, "createdAt", TestConstants.LISTING_DATE);
        return listing;
    }

    public static Listing createListing(Trainer trainer, Pokemon pokemon) {
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

    public static CreateListingRequest createListingRequest() {
        return new CreateListingRequest(
                TestConstants.POKEMON_ID,
                TestConstants.POKEMON_PRICE,
                TestConstants.POKEMON_DESCRIPTION
        );
    }

    public static UpdateListingRequest createUpdateListingRequest() {
        return new UpdateListingRequest(
                TestConstants.UPDATED_PRICE,
                TestConstants.UPDATED_DESCRIPTION
        );
    }

    public static Trainer createTrainer() {
        Trainer trainer = Trainer.create(TestConstants.TRAINER_NAME,
                TestConstants.TRAINER_EMAIL);
        ReflectionTestUtils.setField(trainer, "id", TestConstants.TRAINER_ID);
        return trainer;
    }

    public static Pokemon createPokemon(Trainer trainer) {
        Pokemon pokemon = Pokemon.create(
                TestConstants.POKEMON_SPECIES,
                TestConstants.POKEMON_LEVEL,
                trainer);
        ReflectionTestUtils.setField(pokemon, "id", TestConstants.POKEMON_ID);
        return pokemon;
    }

    public static TrainerResponse createTrainerResponse() {
        return new TrainerResponse(
                TestConstants.TRAINER_ID,
                TestConstants.TRAINER_NAME);
    }

    public static PokemonResponse createPokemonResponse() {
        return new PokemonResponse(
                TestConstants.POKEMON_ID,
                TestConstants.POKEMON_SPECIES,
                TestConstants.POKEMON_LEVEL
        );
    }
}
