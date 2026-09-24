package org.example.poketrade.mapper;

import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.dto.PokemonResponse;
import org.example.poketrade.dto.TrainerResponse;
import org.example.poketrade.entity.Listing;
import org.example.poketrade.entity.Pokemon;
import org.example.poketrade.entity.Trainer;
import org.springframework.stereotype.Component;

@Component
public class ListingMapper {

    public ListingResponse toResponse(Listing listing) {
        return new ListingResponse(
                listing.getId(),
                toResponse(listing.getSeller()),
                toResponse(listing.getPokemon()),
                listing.getPrice(),
                listing.getCreatedAt()
        );
    }

    public TrainerResponse toResponse(Trainer trainer) {
        return new TrainerResponse(
                trainer.getId(),
                trainer.getUsername()
        );
    }

    public PokemonResponse toResponse(Pokemon pokemon) {
        return new PokemonResponse(
                pokemon.getId(),
                pokemon.getSpecies(),
                pokemon.getLevel()
        );
    }
}
