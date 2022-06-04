package com.cards.serviceImplementation;

import com.cards.dto.request.AddHistoryDTO;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.HistoryModel;
import com.cards.dto.response.PageResponse;
import com.cards.entity.History;
import com.cards.entity.User;
import com.cards.enums.Action;
import com.cards.repository.HistoryRepository;
import com.cards.serviceInterface.IHistoryService;
import com.cards.serviceInterface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HistoryService implements IHistoryService {
    private final IUserService userService;
    private final HistoryRepository historyRepository;


    public History getHistory(UUID historyId) {
        return historyRepository.findById(historyId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("History with id %s doesn't exists", historyId))
        );
    }

    public PageResponse<HistoryModel> getUserHistory(PageRequestDTO pageRequestDTO, UUID userId) {
        return new PageResponse<>(historyRepository.findAllByUserUserId(userId, pageRequestDTO.getRequest(History.class))
                .map(history -> {
                    if (history.getAction().equals(Action.checkOffer))
                        return new HistoryModel(
                                history.getAction().name(),
                                history.getHistoryId(),
                                history.getLink()
                        );
                    return new HistoryModel(
                            history.getAction().name(),
                            history.getHistoryId(),
                            history.getCard()
                    );
                })

        );
    }

    public void addHistory(AddHistoryDTO historyDTO) {

        User user = userService.getUser(UUID.fromString(historyDTO.getUserId()));

        History history;

        if (!historyDTO.getAction().equals(Action.checkOffer.name()))
            history = new History(
                    Action.valueOf(historyDTO.getAction()),
                    UUID.fromString(historyDTO.getContent()),
                    LocalDateTime.now(),
                    user
            );

        else history = new History(
                Action.valueOf(historyDTO.getAction()),
                historyDTO.getContent(),
                LocalDateTime.now(),
                user
        );

        historyRepository.save(history);

    }

    public void deleteHistory(UUID historyId) {

        getHistory(historyId);

        historyRepository.deleteById(historyId);

    }
}
