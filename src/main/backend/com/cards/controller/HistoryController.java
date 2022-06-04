package com.cards.controller;

import com.cards.dto.request.AddHistoryDTO;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.HistoryModel;
import com.cards.dto.response.PageResponse;
import com.cards.serviceInterface.IHistoryService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/history")
@AllArgsConstructor
public class HistoryController {
    private final IHistoryService historyService;

    @GetMapping("{userId}")
    public PageResponse<HistoryModel> getUserHistory(@PathVariable UUID userId, @RequestParam Integer page, @RequestParam Integer pageLimit,
                                                     @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                                     @RequestParam(required = false, defaultValue = "dateTime") String sortBy) {
        return historyService.getUserHistory(new PageRequestDTO(page, pageLimit, sortDir, sortBy), userId);
    }

    @PostMapping
    public void addUserHistory(@RequestBody AddHistoryDTO historyDTO){
        historyService.addHistory(historyDTO);
    }

    @DeleteMapping("{historyId}")
    public void deleteHistory(@PathVariable UUID historyId) {
        historyService.deleteHistory(historyId);
    }


}
