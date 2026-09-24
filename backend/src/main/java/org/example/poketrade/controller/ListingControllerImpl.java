package org.example.poketrade.controller;

import java.util.List;

import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.service.ListingService;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ListingControllerImpl implements ListingController {

    private final ListingService listingService;

    @Override
    public List<ListingResponse> getAll() {
        return listingService.getAll();
    }
}
