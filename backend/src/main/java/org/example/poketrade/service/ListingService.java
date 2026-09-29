package org.example.poketrade.service;

import java.util.List;

import org.example.poketrade.dto.CreateListingRequest;
import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.dto.UpdateListingRequest;

public interface ListingService {

    ListingResponse create(CreateListingRequest request);

    ListingResponse getById(Long id);

    List<ListingResponse> getAll();

    ListingResponse update(Long id, UpdateListingRequest request);

    void delete(Long id);
}
