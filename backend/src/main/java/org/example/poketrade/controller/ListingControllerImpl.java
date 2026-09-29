package org.example.poketrade.controller;

import java.util.List;

import org.example.poketrade.dto.CreateListingRequest;
import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.dto.UpdateListingRequest;
import org.example.poketrade.service.ListingService;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ListingControllerImpl implements ListingController {

    private final ListingService listingService;

    @Override
    public ListingResponse create(CreateListingRequest request) {
        return listingService.create(request);
    }

    @Override
    public ListingResponse getById(Long id) {
        return listingService.getById(id);
    }

    @Override
    public List<ListingResponse> getAll() {
        return listingService.getAll();
    }

    @Override
    public ListingResponse update(Long id, UpdateListingRequest request) {
        return listingService.update(id, request);
    }

    @Override
    public void delete(Long id) {
        listingService.delete(id);
    }
}
