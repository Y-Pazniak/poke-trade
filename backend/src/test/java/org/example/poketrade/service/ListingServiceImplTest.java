package org.example.poketrade.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.example.poketrade.TestConstants;
import org.example.poketrade.builder.TestBuilder;
import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.entity.Listing;
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
        Listing listing = TestBuilder.createListing();
        ListingResponse expected = TestBuilder.createListingResponse();
        when(listingRepository.findById(TestConstants.LISTING_ID)).thenReturn(Optional.of(listing));
        when(listingMapper.toResponse(listing)).thenReturn(expected);

        ListingResponse actual = listingService.getById(TestConstants.LISTING_ID);

        assertThat(actual).isEqualTo(expected);
        verify(listingRepository, times(1)).findById(TestConstants.LISTING_ID);
        verify(listingMapper, times(1)).toResponse(listing);
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
}
