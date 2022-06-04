package com.cards.serviceInterface;

import com.cards.entity.GraphicCard;
import com.cards.entity.OnlineOffer;

import java.util.List;

public interface IOnlineOffersService {

    List<OnlineOffer> getCardOnlineOffers(GraphicCard card);
}
