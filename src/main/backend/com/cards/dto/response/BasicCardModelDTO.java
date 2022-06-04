package com.cards.dto.response;

import com.cards.entity.GraphicCard;
import com.cards.entity.LocalOffer;
import com.cards.entity.OnlineOffer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Data
@AllArgsConstructor
public class BasicCardModelDTO {
    private final UUID id;
    private final String cardPhoto;
    private final String title;

    private final Boolean availableLocal;
    private final Integer localStoresNumber;
    private final BigDecimal localStoresLowestPrice;

    private final Boolean availableOnline;
    private final Integer onlineStoresNumber;
    private final BigDecimal onlineStoresLowestPrice;

    private final Boolean isFollowed;

    public static  BasicCardModelDTO convertFromEntity(GraphicCard card,
                                                       String cardPhoto,
                                                       List<LocalOffer> localOffers,
                                                       List<OnlineOffer> onlineOffers,
                                                       Boolean isFollowed){

        AtomicReference<BigDecimal> localStoresLowestPrice = new AtomicReference<>(new BigDecimal(0));
        AtomicReference<BigDecimal> onlineStoresLowestPrice = new AtomicReference<>(new BigDecimal(0));

        Optional<LocalOffer> minLocalOffer = localOffers.stream().min(Comparator.comparing(LocalOffer::getPrice));
        minLocalOffer.ifPresent(offer -> localStoresLowestPrice.set(offer.getPrice()));

        Optional<OnlineOffer> minOnlineOffer = onlineOffers.stream().min(Comparator.comparing(OnlineOffer::getPrice));
        minOnlineOffer.ifPresent(offer -> onlineStoresLowestPrice.set(offer.getPrice()));

        return new BasicCardModelDTO(
                card.getCardId(),
                cardPhoto,
                card.getTitle(),
                !localOffers.isEmpty(),
                localOffers.size(),
                localStoresLowestPrice.get(),
                !onlineOffers.isEmpty(),
                onlineOffers.size(),
                onlineStoresLowestPrice.get(),
                isFollowed
        );
    }

}
