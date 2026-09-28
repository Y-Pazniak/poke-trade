package org.example.poketrade.controller;

import java.util.List;

import org.example.poketrade.dto.CreateListingRequest;
import org.example.poketrade.dto.ListingResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Listings", description = "Poketrade listing shenanigans")
@RequestMapping("/api/v1/listings")
public interface ListingController {

    @Operation(summary = "Get all listings")
    @GetMapping
    List<ListingResponse> getAll();

    @Operation(summary = "Post a listing")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ListingResponse create(@Valid @RequestBody CreateListingRequest createListingRequest);
}
