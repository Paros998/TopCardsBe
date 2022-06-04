package com.cards.serviceInterface;

import com.cards.entity.GraphicCard;
import com.cards.entity.LocalOffer;

import java.util.List;

public interface ILocalOffersService {

    List<LocalOffer> getCardLocalOffers(GraphicCard card);

}
