package org.example.poketrade.controller;

import java.util.List;

import org.example.poketrade.dto.ListingResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Listings", description = "Poketrade listing shenanigans")
@RequestMapping("/api/v1/listings")
public interface ListingController {

    @Operation(summary = "Get all listings")
    @GetMapping
    List<ListingResponse> getAll();
}
