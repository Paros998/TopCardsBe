package com.cards.serviceInterface;

import com.cards.dto.request.AddHistoryDTO;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.HistoryModel;
import com.cards.dto.response.PageResponse;
import com.cards.entity.History;

import java.util.UUID;

public interface IHistoryService {

    History getHistory(UUID historyId);

    PageResponse<HistoryModel> getUserHistory(PageRequestDTO pageRequestDTO, UUID userId);

    void addHistory(AddHistoryDTO historyDTO);

    void deleteHistory(UUID historyId);

}
