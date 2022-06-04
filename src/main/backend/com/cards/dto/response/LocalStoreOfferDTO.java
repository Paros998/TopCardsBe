package com.cards.dto.response;

import com.cards.entity.LocalOffer;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class LocalStoreOfferDTO {
    private final String name;
    private final String address;
    private final String phone;
    private final BigDecimal price;
    private final String shopWebsite;

    public static LocalStoreOfferDTO convertFromEntity(LocalOffer offer){

        return new LocalStoreOfferDTO(
          offer.getStore().getName(),
                offer.getAddress(),
                offer.getStore().getPhone(),
                offer.getPrice(),
                offer.getStore().getWebsite()
        );

    }
}
