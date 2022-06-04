package com.cards.serviceInterface;

import com.cards.dto.request.AddGetGraphicCardBody;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.BasicCardModelDTO;
import com.cards.dto.response.PageResponse;
import com.cards.dto.response.UserConsensus;
import com.cards.entity.GraphicCard;

import java.util.List;
import java.util.UUID;

public interface ICardsService {

    GraphicCard getCard(UUID cardId);

    String getCardPhoto(UUID cardId);

    PageResponse<BasicCardModelDTO> getCards(PageRequestDTO pageRequestDTO,
                                             UUID userId,
                                             Boolean availableLocal,
                                             Boolean unavailableLocal,
                                             Boolean availableOnline,
                                             Boolean unavailableOnline,
                                             List<String> manufacturer,
                                             List<String> technology,
                                             List<String> memory,
                                             List<String> memoryType,
                                             List<String> outputsType,
                                             List<String> memoryBus);

    List<BasicCardModelDTO> getCards(List<UUID> cardIds);

    List<BasicCardModelDTO> getSuggestedCards(UUID userId);

    List<BasicCardModelDTO> getNotSuggestedCards();

    UUID addCard(AddGetGraphicCardBody cardBody);


}
