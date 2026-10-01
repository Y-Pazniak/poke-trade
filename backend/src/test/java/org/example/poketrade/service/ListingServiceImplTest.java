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
import org.example.poketrade.dto.ListingResponse;
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
