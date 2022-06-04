package com.cards.serviceImplementation;

import com.cards.entity.GraphicCard;
import com.cards.entity.LocalOffer;
import com.cards.enums.SearchOperation;
import com.cards.repository.LocalOfferRepository;
import com.cards.search.SearchCriteria;
import com.cards.search.Specifications;
import com.cards.serviceInterface.ILocalOffersService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LocalOffersService implements ILocalOffersService {
    private final LocalOfferRepository offerRepository;


    public List<LocalOffer> getCardLocalOffers(GraphicCard card) {

        Specifications<LocalOffer> findByCard = new Specifications<LocalOffer>()
                .and(new SearchCriteria("card",card, SearchOperation.EQUAL));

        return offerRepository.findAll(findByCard);
    }
}
