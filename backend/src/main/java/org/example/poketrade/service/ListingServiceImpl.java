package org.example.poketrade.service;

import java.util.List;
import java.util.Objects;

import org.example.poketrade.dto.CreateListingRequest;
import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.dto.UpdateListingRequest;
import org.example.poketrade.entity.Listing;
import org.example.poketrade.entity.Pokemon;
import org.example.poketrade.entity.Trainer;
import org.example.poketrade.exception.BusinessException;
import org.example.poketrade.exception.NotFoundException;
import org.example.poketrade.mapper.ListingMapper;
import org.example.poketrade.repository.ListingRepository;
import org.example.poketrade.repository.PokemonRepository;
import org.example.poketrade.repository.TrainerRepository;
import org.example.poketrade.security.CurrentUserProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final TrainerRepository trainerRepository;
    private final PokemonRepository pokemonRepository;
    private final ListingMapper listingMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    @Transactional
    public ListingResponse create(CreateListingRequest request) {
        Long trainerId = currentUserProvider.getCurrentUserId();
        Long pokemonId = request.pokemonId();

        Trainer trainer = trainerRepository.findById(trainerId).orElseThrow(() -> NotFoundException.trainer(trainerId));
        Pokemon pokemon = pokemonRepository.findById(pokemonId).orElseThrow(() -> NotFoundException.pokemon(pokemonId));

        if (!Objects.equals(trainerId, pokemon.getOwner().getId())) {
            throw new BusinessException(
                    "Pokemon does not belong to this trainer");
        }

        Listing listing = listingRepository.save(Listing.create(trainer, pokemon, request.price(),
                request.description()));
        return listingMapper.toResponse(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public ListingResponse getById(Long id) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> NotFoundException.listing(id));

        return listingMapper.toResponse(listing);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListingResponse> getAll() {
        return listingRepository.findAll().stream().map(listingMapper::toResponse).toList();
    }

    @Override
    @Transactional
    public ListingResponse update(Long id, UpdateListingRequest request) {
        Listing listing = listingRepository.findById(id).orElseThrow(() -> NotFoundException.listing(id));

        if (!Objects.equals(currentUserProvider.getCurrentUserId(), listing.getSeller().getId())) {
            throw new BusinessException("You are not allowed to update another trainer listing.");
        }

        listing.update(request.price(), request.description());

        return listingMapper.toResponse(listing);
    }
}
