package org.example.poketrade.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.example.poketrade.TestConstants;
import org.example.poketrade.builder.TestBuilder;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ListingServiceImplTest {

    @Mock
    private ListingRepository listingRepository;
    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private PokemonRepository pokemonRepository;
    @Mock
    private ListingMapper listingMapper;
    @Mock
    private CurrentUserProvider currentUserProvider;

    @InjectMocks
    private ListingServiceImpl listingService;

    @Test
    void create_shouldCreateListing_whenTrainerAndPokemonExistAndPokemonBelongsToTrainer() {
        CreateListingRequest request = TestBuilder.createListingRequest();
        ListingResponse expected = TestBuilder.createListingResponse();

        Trainer trainer = mock(Trainer.class);
        Pokemon pokemon = mock(Pokemon.class);
        Listing savedListing = TestBuilder.createListing();

        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.TRAINER_ID);
        when(trainerRepository.findById(TestConstants.TRAINER_ID)).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(TestConstants.POKEMON_ID)).thenReturn(Optional.of(pokemon));
        when(pokemon.getOwner()).thenReturn(trainer);
        when(trainer.getId()).thenReturn(TestConstants.TRAINER_ID);
        when(listingRepository.save(any(Listing.class))).thenReturn(savedListing);
        when(listingMapper.toResponse(savedListing)).thenReturn(expected);

        ListingResponse actual = listingService.create(request);

        assertThat(actual).isEqualTo(expected);

        ArgumentCaptor<Listing> captor = ArgumentCaptor.forClass(Listing.class);
        verify(listingRepository, times(1)).save(captor.capture());

        Listing toSave = captor.getValue();
        assertThat(toSave.getSeller()).isEqualTo(trainer);
        assertThat(toSave.getPokemon()).isEqualTo(pokemon);
        assertThat(toSave.getPrice()).isEqualTo(request.price());
        assertThat(toSave.getDescription()).isEqualTo(request.description());

        verify(currentUserProvider, times(1)).getCurrentUserId();
        verify(trainerRepository, times(1)).findById(TestConstants.TRAINER_ID);
        verify(pokemonRepository, times(1)).findById(request.pokemonId());
        verify(listingMapper, times(1)).toResponse(savedListing);
    }

    @Test
    void create_shouldThrowNotFoundException_whenTrainerDoesNotExist() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.OTHER_TRAINER_ID);
        when(trainerRepository.findById(TestConstants.OTHER_TRAINER_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listingService.create(TestBuilder.createListingRequest()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.TRAINER_NOT_FOUND_FORMAT.formatted(TestConstants.OTHER_TRAINER_ID));

        verify(currentUserProvider, times(1)).getCurrentUserId();
        verify(trainerRepository, times(1)).findById(TestConstants.OTHER_TRAINER_ID);
        verify(listingRepository, never()).save(any());
        verifyNoInteractions(pokemonRepository, listingMapper);
    }

    @Test
    void create_shouldThrowNotFoundException_whenPokemonDoesNotExist() {
        Trainer trainer = mock(Trainer.class);
        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.TRAINER_ID);
        when(trainerRepository.findById(TestConstants.TRAINER_ID)).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(TestConstants.POKEMON_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listingService.create(TestBuilder.createListingRequest()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.POKEMON_NOT_FOUND_FORMAT.formatted(TestConstants.POKEMON_ID));

        verify(currentUserProvider, times(1)).getCurrentUserId();
        verify(trainerRepository, times(1)).findById(TestConstants.TRAINER_ID);
        verify(pokemonRepository, times(1)).findById(TestConstants.POKEMON_ID);
        verify(listingRepository, never()).save(any());
        verifyNoInteractions(listingMapper);
    }

    @Test
    void create_shouldThrowBusinessException_whenPokemonDoesNotBelongToTrainer() {
        Trainer trainer = mock(Trainer.class);
        Pokemon pokemon = mock(Pokemon.class);

        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.OTHER_TRAINER_ID);
        when(trainerRepository.findById(TestConstants.OTHER_TRAINER_ID)).thenReturn(Optional.of(trainer));
        when(pokemonRepository.findById(TestConstants.POKEMON_ID)).thenReturn(Optional.of(pokemon));
        when(pokemon.getOwner()).thenReturn(trainer);
        when(trainer.getId()).thenReturn(TestConstants.TRAINER_ID);

        assertThatThrownBy(() -> listingService.create(TestBuilder.createListingRequest()))
                .hasMessage(BusinessException.WRONG_OWNER)
                .isInstanceOf(BusinessException.class);

        verify(currentUserProvider, times(1)).getCurrentUserId();
        verify(trainerRepository, times(1)).findById(TestConstants.OTHER_TRAINER_ID);
        verify(pokemonRepository, times(1)).findById(TestConstants.POKEMON_ID);
        verify(listingRepository, never()).save(any());
        verifyNoInteractions(listingMapper);
    }

    @Test
    void getById_shouldReturnListingResponse_whenListingExists() {
        Listing expectedListing = TestBuilder.createListing();
        ListingResponse expectedResponse = TestBuilder.createListingResponse();
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.of(expectedListing));
        when(listingMapper.toResponse(expectedListing)).thenReturn(expectedResponse);

        ListingResponse actualResponse = listingService.getById(TestConstants.LISTING_ID);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(listingMapper, times(1)).toResponse(expectedListing);
        verifyNoInteractions(pokemonRepository, trainerRepository, currentUserProvider);
    }

    @Test
    void getById_shouldThrowNotFoundException_whenListingDoesNotExist() {
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listingService.getById(TestConstants.LISTING_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.LISTING_NOT_FOUND_FORMAT.formatted(TestConstants.LISTING_ID));

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verifyNoInteractions(pokemonRepository, trainerRepository, currentUserProvider, listingMapper);
    }

    @Test
    void getAll_shouldReturnListResponse() {
        Listing expectedListing1 = TestBuilder.createListing();
        Listing expectedListing2 = TestBuilder.createListing();
        ListingResponse expectedResponse1 = TestBuilder.createListingResponse();
        ListingResponse expectedResponse2 = TestBuilder.createListingResponse();

        when(listingRepository.findAll()).thenReturn(List.of(expectedListing1, expectedListing2));
        when(listingMapper.toResponse(expectedListing1)).thenReturn(expectedResponse1);
        when(listingMapper.toResponse(expectedListing2)).thenReturn(expectedResponse2);

        List<ListingResponse> actual = listingService.getAll();

        assertThat(actual).containsExactly(expectedResponse1, expectedResponse2);
        verify(listingRepository, times(1)).findAll();
        verify(listingMapper).toResponse(expectedListing1);
        verify(listingMapper).toResponse(expectedListing2);
        verifyNoInteractions(pokemonRepository, trainerRepository, currentUserProvider);
    }

    @Test
    void getAll_shouldReturnEmptyList_whenNoListings() {
        when(listingRepository.findAll()).thenReturn(List.of());

        List<ListingResponse> actual = listingService.getAll();

        assertThat(actual).isEmpty();
        verify(listingRepository, times(1)).findAll();
        verifyNoInteractions(pokemonRepository, trainerRepository, currentUserProvider, listingMapper);
    }

    @Test
    void update_shouldReturnListingResponse_whenUpdatedSuccessfully() {
        Trainer trainer = mock(Trainer.class);
        Pokemon pokemon = mock(Pokemon.class);
        UpdateListingRequest request = TestBuilder.createUpdateListingRequest();
        ListingResponse expectedResponse = TestBuilder.createListingResponse();
        Listing listing = TestBuilder.createListing(trainer, pokemon);

        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.of(listing));
        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.TRAINER_ID);
        when(trainer.getId()).thenReturn(TestConstants.TRAINER_ID);
        when(listingMapper.toResponse(listing)).thenReturn(expectedResponse);

        ListingResponse actualResponse = listingService.update(TestConstants.LISTING_ID, request);

        assertThat(actualResponse).isEqualTo(expectedResponse);
        assertThat(listing.getPrice()).isEqualTo(request.price());
        assertThat(listing.getDescription()).isEqualTo(request.description());

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(currentUserProvider, times(1)).getCurrentUserId();
        verify(listingMapper, times(1)).toResponse(listing);
        verifyNoInteractions(pokemonRepository, trainerRepository);
    }

    @Test
    void update_shouldThrowNotFoundException_whenListingDoesNotExist() {
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listingService.update(TestConstants.LISTING_ID, TestBuilder.createUpdateListingRequest()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.LISTING_NOT_FOUND_FORMAT.formatted(TestConstants.LISTING_ID));

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verifyNoInteractions(pokemonRepository, trainerRepository, currentUserProvider, listingMapper);
    }

    @Test
    void update_shouldThrowBusinessException_whenListingDoesNotBelongToTheTrainer() {
        Trainer trainer = mock(Trainer.class);
        Pokemon pokemon = mock(Pokemon.class);
        Listing listing = TestBuilder.createListing(trainer, pokemon);

        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.of(listing));
        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.TRAINER_ID);
        when(listing.getSeller().getId()).thenReturn(TestConstants.OTHER_TRAINER_ID);

        assertThatThrownBy(() -> listingService.update(TestConstants.LISTING_ID, TestBuilder.createUpdateListingRequest()))
                .isInstanceOf(BusinessException.class)
                .hasMessage(BusinessException.WRONG_OWNER);

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(currentUserProvider, times(1)).getCurrentUserId();
        verifyNoInteractions(pokemonRepository, trainerRepository, listingMapper);
    }

    @Test
    void delete_shouldDelete_whenOwnerDeletesOwnListing() {
        Trainer trainer = mock(Trainer.class);
        Pokemon pokemon = mock(Pokemon.class);
        Listing listing = TestBuilder.createListing(trainer, pokemon);

        when(trainer.getId()).thenReturn(TestConstants.TRAINER_ID);
        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.TRAINER_ID);
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.of(listing));

        listingService.delete(TestConstants.LISTING_ID);

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(listingRepository, times(1)).delete(listing);
        verify(currentUserProvider, times(1)).getCurrentUserId();
        verifyNoInteractions(pokemonRepository, trainerRepository, listingMapper);
    }

    @Test
    void delete_shouldThrowNotFoundException_whenListingDoesNotExist() {
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> listingService.delete(TestConstants.LISTING_ID))
                .isInstanceOf(NotFoundException.class)
                .hasMessage(NotFoundException.LISTING_NOT_FOUND_FORMAT.formatted(TestConstants.LISTING_ID));

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(listingRepository, never()).delete(any());
        verifyNoInteractions(pokemonRepository, trainerRepository, currentUserProvider, listingMapper);
    }

    @Test
    void delete_shouldThrowBusinessException_whenListingDoesNotBelongToTheTrainer() {
        Trainer trainer = mock(Trainer.class);
        Pokemon pokemon = mock(Pokemon.class);
        Listing listing = TestBuilder.createListing(trainer, pokemon);

        when(trainer.getId()).thenReturn(TestConstants.TRAINER_ID);
        when(currentUserProvider.getCurrentUserId()).thenReturn(TestConstants.OTHER_TRAINER_ID);
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.of(listing));

        assertThatThrownBy(() -> listingService.delete(TestConstants.LISTING_ID))
                .isInstanceOf(BusinessException.class)
                .hasMessage(BusinessException.WRONG_OWNER);

        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(listingRepository, never()).delete(any());
        verifyNoInteractions(pokemonRepository, trainerRepository, listingMapper);
    }
}
