package org.example.poketrade.service;

import java.util.List;

import org.example.poketrade.dto.ListingResponse;

public interface ListingService {
    List<ListingResponse> getAll();
}
