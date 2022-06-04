package com.cards.controller;

import com.cards.dto.request.AddGetGraphicCardBody;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.BasicCardModelDTO;
import com.cards.dto.response.PageResponse;
import com.cards.dto.response.UserConsensus;
import com.cards.serviceInterface.ICardsService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/cards")
@AllArgsConstructor
public class CardsController {
    private final ICardsService cardsService;

    @GetMapping("{cardId}")
    public AddGetGraphicCardBody getCard(@PathVariable UUID cardId) {
        return AddGetGraphicCardBody.convertFromEntity(cardsService.getCard(cardId));
    }

    @GetMapping("{cardId}/title")
    public String getCardTitle(@PathVariable UUID cardId) {
        return cardsService.getCard(cardId).getTitle();
    }

    @GetMapping
    public PageResponse<BasicCardModelDTO> getCards(@RequestParam Integer page, @RequestParam Integer pageLimit,
                                                    @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                                    @RequestParam(required = false, defaultValue = "title") String sortBy,
                                                    @RequestParam(required = false) UUID userId,
                                                    @RequestParam(required = false) Boolean availableLocal,
                                                    @RequestParam(required = false) Boolean unavailableLocal,
                                                    @RequestParam(required = false) Boolean availableOnline,
                                                    @RequestParam(required = false) Boolean unavailableOnline,
                                                    @RequestParam(required = false) List<String> manufacturer,
                                                    @RequestParam(required = false) List<String> technology,
                                                    @RequestParam(required = false) List<String> memory,
                                                    @RequestParam(required = false) List<String> memoryType,
                                                    @RequestParam(required = false) List<String> outputsType,
                                                    @RequestParam(required = false) List<String> memoryBus) {
        return cardsService.getCards(
                new PageRequestDTO(page, pageLimit, sortDir, sortBy),
                userId,
                availableLocal,
                unavailableLocal,
                availableOnline,
                unavailableOnline,
                manufacturer,
                technology,
                memory,
                memoryType,
                outputsType,
                memoryBus);
    }

    @GetMapping("not-suggested")
    public List<BasicCardModelDTO> getNotSuggestedCards() {
        return cardsService.getNotSuggestedCards();
    }

    @GetMapping("suggested")
    public List<BasicCardModelDTO> getSuggestedCards(@RequestParam(required = false) UUID userId) {
        return cardsService.getSuggestedCards(userId);
    }

    @PostMapping
    public UUID addGraphicCard(@RequestBody AddGetGraphicCardBody cardBody) {
        return cardsService.addCard(cardBody);
    }
}
