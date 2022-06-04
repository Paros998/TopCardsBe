package com.cards.serviceImplementation;

import com.cards.entity.GraphicCard;
import com.cards.entity.OnlineOffer;
import com.cards.enums.SearchOperation;
import com.cards.repository.OnlineOfferRepository;
import com.cards.search.SearchCriteria;
import com.cards.search.Specifications;
import com.cards.serviceInterface.IOnlineOffersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OnlineOffersService implements IOnlineOffersService {
    private final OnlineOfferRepository offerRepository;

    public List<OnlineOffer> getCardOnlineOffers(GraphicCard card) {

        Specifications<OnlineOffer> findByCard = new Specifications<OnlineOffer>()
                .and(new SearchCriteria("card", card, SearchOperation.EQUAL));

        return offerRepository.findAll(findByCard);

    }
}
