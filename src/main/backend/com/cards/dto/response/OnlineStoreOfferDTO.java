package com.cards.dto.response;

import com.cards.entity.OnlineOffer;
import com.cards.entity.Store;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OnlineStoreOfferDTO {
    private final UUID photoId;
    private final String name;
    private final Float ratingScore;
    private final Integer ratingCount;
    private final BigDecimal price;
    private final Boolean hasFreeShipping;
    private final String offerWebsite;

    public static OnlineStoreOfferDTO convertFromEntity(OnlineOffer offer, UUID storePhotoID){
        Store store = offer.getStore();

        return new OnlineStoreOfferDTO(
                storePhotoID,
                store.getName(),
                store.getRatingScore(),
                store.getRatingCount(),
                offer.getPrice(),
                offer.getHasFreeShipping(),
                offer.getOfferWebsite()
        );
    }
}
