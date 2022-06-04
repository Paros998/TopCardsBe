package com.cards.controller;

import com.cards.dto.response.LocalStoreOfferDTO;
import com.cards.entity.LocalOffer;
import com.cards.serviceInterface.ICardsService;
import com.cards.serviceInterface.ILocalOffersService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/local-offers")
@AllArgsConstructor
public class LocalOfferController {
    private final ILocalOffersService offersService;
    private final ICardsService cardsService;

    @GetMapping("card/{cardId}")
    public List<LocalStoreOfferDTO> getCardLocalOffers(@PathVariable UUID cardId) {
        return offersService.getCardLocalOffers(cardsService.getCard(cardId))
                .stream()
                .sorted(Comparator.comparing(LocalOffer::getPrice))
                .map(LocalStoreOfferDTO::convertFromEntity)
                .collect(Collectors.toList());
    }
}
