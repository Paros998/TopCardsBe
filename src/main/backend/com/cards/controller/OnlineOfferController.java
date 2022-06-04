package com.cards.controller;

import com.cards.dto.response.OnlineStoreOfferDTO;
import com.cards.entity.LocalOffer;
import com.cards.entity.OnlineOffer;
import com.cards.serviceInterface.ICardsService;
import com.cards.serviceInterface.IOnlineOffersService;
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
@RequestMapping("/api/v1/online-offers")
@AllArgsConstructor
public class OnlineOfferController {
    private final IOnlineOffersService offersService;
    private final ICardsService cardsService;
    private static final UUID NO_STORE_IMAGE = UUID.fromString("0a78c17b-d6f9-4cbb-ae17-8ccf2a3d8696");

    @GetMapping("card/{cardId}")
    public List<OnlineStoreOfferDTO> getCardOnlineOffers(@PathVariable UUID cardId) {
        return offersService.getCardOnlineOffers(cardsService.getCard(cardId))
                .stream()
                .sorted(Comparator.comparing(OnlineOffer::getPrice))
                .map(offer -> OnlineStoreOfferDTO.convertFromEntity(
                                offer,
                                offer.getStore().getStorePhoto() == null ? NO_STORE_IMAGE : offer.getStore().getStorePhoto().getFileId()
                        )
                )
                .collect(Collectors.toList());
    }


}

