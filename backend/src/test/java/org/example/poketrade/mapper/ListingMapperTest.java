    package org.example.poketrade.mapper;

    import static org.assertj.core.api.Assertions.assertThat;

    import org.example.poketrade.builder.TestBuilder;
    import org.example.poketrade.dto.ListingResponse;
    import org.example.poketrade.dto.PokemonResponse;
    import org.example.poketrade.dto.TrainerResponse;
    import org.example.poketrade.entity.Listing;
    import org.example.poketrade.entity.Pokemon;
    import org.example.poketrade.entity.Trainer;
    import org.junit.jupiter.api.Test;

    class ListingMapperTest {

        private final ListingMapper mapper = new ListingMapper();

        @Test
        void toResponse_shouldMapAllListingFields() {
            Listing listing = TestBuilder.createListing();
            ListingResponse expectedResponse = TestBuilder.createListingResponse();

            ListingResponse actualResponse = mapper.toResponse(listing);

            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void toResponse_shouldMapTrainerFields() {
            Trainer trainer = TestBuilder.createTrainer();
            TrainerResponse expectedResponse = TestBuilder.createTrainerResponse();

            TrainerResponse actualResponse = mapper.toResponse(trainer);

            assertThat(actualResponse).isEqualTo(expectedResponse);
        }

        @Test
        void toResponse_shouldMapPokemonFields() {
            Trainer trainer = TestBuilder.createTrainer();
            Pokemon pokemon = TestBuilder.createPokemon(trainer);
            PokemonResponse expectedResponse = TestBuilder.createPokemonResponse();

            PokemonResponse actualResponse = mapper.toResponse(pokemon);

            assertThat(actualResponse).isEqualTo(expectedResponse);
        }
    }
