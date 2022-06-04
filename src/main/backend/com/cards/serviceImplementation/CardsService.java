package com.cards.serviceImplementation;

import com.cards.dto.request.AddGetGraphicCardBody;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.BasicCardModelDTO;
import com.cards.dto.response.PageResponse;
import com.cards.entity.File;
import com.cards.entity.GraphicCard;
import com.cards.entity.SuggestedCards;
import com.cards.enums.*;
import com.cards.repository.GraphicCardRepository;
import com.cards.repository.SuggestedCardsRepository;
import com.cards.search.SearchCriteria;
import com.cards.search.Specifications;
import com.cards.serviceInterface.*;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CardsService implements ICardsService {
    private final IFileService fileService;
    private final ILocalOffersService localOffersService;
    private final IOnlineOffersService onlineOffersService;
    private final IUserService userService;

    private final GraphicCardRepository cardRepository;
    private final SuggestedCardsRepository suggestedCardsRepository;

    private final static UUID ProductPhoto = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    public GraphicCard getCard(UUID cardId) {
        return cardRepository.findById(cardId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Graphic Card with id %s not found", cardId))
        );
    }

    public String getCardPhoto(UUID cardId) {

        File cardPhoto = getCard(cardId).getCardPhoto();

        if (cardPhoto == null)
            return fileService.getFileUrl(ProductPhoto);

        return fileService.getFileUrl(cardPhoto.getFileId());
    }

    public PageResponse<BasicCardModelDTO> getCards(PageRequestDTO pageRequestDTO,
                                                    UUID userId,
                                                    Boolean availableLocal,
                                                    Boolean unavailableLocal,
                                                    Boolean availableOnline,
                                                    Boolean unavailableOnline,
                                                    List<String> manufacturer,
                                                    List<String> technology,
                                                    List<String> memoryAmount,
                                                    List<String> memoryType,
                                                    List<String> outputsType,
                                                    List<String> memoryBus) {

        Specification<GraphicCard> cardSpecifications = new Specifications<>();

        if (manufacturer != null && !manufacturer.isEmpty()) {
            Specifications<GraphicCard> manufacturerSpec = new Specifications<>();

            manufacturer.forEach(entry -> manufacturerSpec.or(new SearchCriteria("manufacturer", Manufacturer.valueOf(entry), SearchOperation.EQUAL)));

            cardSpecifications = cardSpecifications.and(manufacturerSpec);

        }

        if (technology != null && !technology.isEmpty()) {
            Specifications<GraphicCard> technologySpec = new Specifications<>();

            technology.forEach(entry -> technologySpec.or(new SearchCriteria("technology", Technology.valueOf(entry), SearchOperation.EQUAL)));

            cardSpecifications = cardSpecifications.and(technologySpec);
        }

        if (memoryAmount != null && !memoryAmount.isEmpty()) {
            Specifications<GraphicCard> memoryAmountSpec = new Specifications<>();

            memoryAmount.forEach(entry -> memoryAmountSpec.or(new SearchCriteria("memoryAmount", MemoryAmount.getMemoryAmount(entry), SearchOperation.EQUAL)));

            cardSpecifications = cardSpecifications.and(memoryAmountSpec);
        }

        if (memoryType != null && !memoryType.isEmpty()) {
            Specifications<GraphicCard> memoryTypeSpec = new Specifications<>();

            memoryType.forEach(entry -> memoryTypeSpec.or(new SearchCriteria("typeOfMemory", MemoryType.valueOf(entry), SearchOperation.EQUAL)));

            cardSpecifications = cardSpecifications.and(memoryTypeSpec);
        }

        if (outputsType != null && !outputsType.isEmpty()) {
            Specifications<GraphicCard> outputsTypeSpec = new Specifications<>();

            outputsType.forEach(entry -> outputsTypeSpec.or(new SearchCriteria("typeOfOutputs", entry, SearchOperation.IS_MEMBER)));

            cardSpecifications = cardSpecifications.and(outputsTypeSpec);
        }

        if (memoryBus != null && !memoryBus.isEmpty()) {
            Specifications<GraphicCard> memoryBusSpec = new Specifications<>();

            memoryBus.forEach(entry -> memoryBusSpec.or(new SearchCriteria("memoryBus", MemoryBus.getMemoryBus(entry), SearchOperation.EQUAL)));

            cardSpecifications = cardSpecifications.and(memoryBusSpec);
        }

        Specifications<GraphicCard> availabilitySpec = new Specifications<>();

        if (availableLocal != null && availableLocal)
            availabilitySpec.or(new SearchCriteria("localOfferList", SearchOperation.IS_NOT_EMPTY));

        if (availableOnline != null && availableOnline)
            availabilitySpec.or(new SearchCriteria("onlineOfferList", SearchOperation.IS_NOT_EMPTY));

        if (unavailableLocal != null && unavailableLocal)
            availabilitySpec.or(new SearchCriteria("localOfferList", SearchOperation.IS_EMPTY));

        if (unavailableOnline != null && unavailableOnline)
            availabilitySpec.or(new SearchCriteria("onlineOfferList", SearchOperation.IS_EMPTY));

        cardSpecifications = cardSpecifications.and(availabilitySpec);


        if (userId != null) {
            List<UUID> followedCards = userService.getUser(userId).getFollowedCards();

            return new PageResponse<>(
                    cardRepository.findAll(cardSpecifications, pageRequestDTO.getRequest(GraphicCard.class))
                            .map(card -> BasicCardModelDTO.convertFromEntity(
                                    card,
                                    getCardPhoto(card.getCardId()),
                                    localOffersService.getCardLocalOffers(card),
                                    onlineOffersService.getCardOnlineOffers(card),
                                    followedCards.contains(card.getCardId())
                            ))
            );

        }

        return new PageResponse<>(
                cardRepository.findAll(cardSpecifications, pageRequestDTO.getRequest(GraphicCard.class))
                        .map(card -> BasicCardModelDTO.convertFromEntity(
                                card,
                                getCardPhoto(card.getCardId()),
                                localOffersService.getCardLocalOffers(card),
                                onlineOffersService.getCardOnlineOffers(card),
                                false
                        ))
        );
    }

    public List<BasicCardModelDTO> getCards(List<UUID> cardIds) {
        return cardRepository.findAllById(cardIds)
                .stream()
                .map(card -> BasicCardModelDTO.convertFromEntity(
                        card,
                        getCardPhoto(card.getCardId()),
                        localOffersService.getCardLocalOffers(card),
                        onlineOffersService.getCardOnlineOffers(card),
                        true
                ))
                .collect(Collectors.toList());
    }

    public List<BasicCardModelDTO> getSuggestedCards(UUID userId) {

        if (userId != null) {
            List<UUID> followedCards = userService.getUser(userId).getFollowedCards();

            return suggestedCardsRepository.findAll()
                    .stream()
                    .map(suggestedCard -> getCard(suggestedCard.getCardId()))
                    .map(card -> BasicCardModelDTO.convertFromEntity(
                            card,
                            getCardPhoto(card.getCardId()),
                            localOffersService.getCardLocalOffers(card),
                            onlineOffersService.getCardOnlineOffers(card),
                            followedCards.contains(card.getCardId())
                    ))
                    .collect(Collectors.toList());

        }

        return suggestedCardsRepository.findAll()
                .stream()
                .map(suggestedCard -> getCard(suggestedCard.getCardId()))
                .map(card -> BasicCardModelDTO.convertFromEntity(
                        card,
                        getCardPhoto(card.getCardId()),
                        localOffersService.getCardLocalOffers(card),
                        onlineOffersService.getCardOnlineOffers(card),
                        false
                ))
                .collect(Collectors.toList());
    }

    public List<BasicCardModelDTO> getNotSuggestedCards() {

        List<SuggestedCards> suggested = suggestedCardsRepository.findAll();

        return cardRepository.findAll()
                .stream()
                .filter(card -> suggested.stream()
                        .noneMatch(suggestedCards -> suggestedCards.getCardId().equals(card.getCardId()))
                )
                .map(card -> BasicCardModelDTO.convertFromEntity(
                        card,
                        getCardPhoto(card.getCardId()),
                        localOffersService.getCardLocalOffers(card),
                        onlineOffersService.getCardOnlineOffers(card),
                        false
                ))
                .collect(Collectors.toList());
    }

    public UUID addCard(AddGetGraphicCardBody cardBody) {

        GraphicCard newCard = new GraphicCard(
                cardBody.getTitle(),
                Technology.valueOf(cardBody.getTechnology()),
                Arrays.asList(cardBody.getTypeOfOutputs()),
                cardBody.getProducentSite(),
                cardBody.getSize(),
                Boolean.valueOf(cardBody.getRtxSupport()),
                Arrays.asList(cardBody.getSupportedLibraries()),
                Connector.getConnectorByName(cardBody.getTypeOfConnector()),
                cardBody.getCuda(),
                cardBody.getPowerConsumption(),
                cardBody.getRecommendedPower(),
                cardBody.getCooling(),
                cardBody.getPowerConnector(),
                cardBody.getCoreClock(),
                MemoryAmount.getMemoryAmount(cardBody.getMemoryAmount()),
                Manufacturer.valueOf(cardBody.getManufacturer()),
                cardBody.getClockMemory(),
                MemoryType.valueOf(cardBody.getTypeOfMemory()),
                cardBody.getProducentCode(),
                MemoryBus.getMemoryBus(cardBody.getMemoryBus()),
                fileService.getFileById(cardBody.getCardPhoto())
        );

        cardRepository.save(newCard);

        return newCard.getCardId();
    }

}
