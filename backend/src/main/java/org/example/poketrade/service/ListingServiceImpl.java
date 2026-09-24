package org.example.poketrade.service;

import java.util.List;

import org.example.poketrade.dto.ListingResponse;
import org.example.poketrade.mapper.ListingMapper;
import org.example.poketrade.repository.ListingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListingServiceImpl implements ListingService {

    private final ListingRepository listingRepository;
    private final ListingMapper listingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ListingResponse> getAll() {
        return listingRepository.findAll().stream().map(listingMapper::toResponse).toList();
    }
}
