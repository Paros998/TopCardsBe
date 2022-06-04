package com.cards.controller;

import com.cards.dto.request.AddReviewModel;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.PageResponse;
import com.cards.dto.response.ReviewModelDTO;
import com.cards.dto.response.ScoreChartResponseDTO;
import com.cards.dto.response.UserConsensus;
import com.cards.serviceInterface.IReviewService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reviews")
@AllArgsConstructor
public class ReviewController {
    private final IReviewService reviewService;

    @GetMapping("card/{cardId}")
    public PageResponse<ReviewModelDTO> getCardReviews(@PathVariable UUID cardId, @RequestParam Integer page, @RequestParam Integer pageLimit,
                                                       @RequestParam(required = false, defaultValue = "asc") String sortDir,
                                                       @RequestParam(required = false, defaultValue = "dateTime") String sortBy) {
        return reviewService.getCardReviews(cardId, new PageRequestDTO(page, pageLimit, sortDir, sortBy));
    }

    @GetMapping("card/{cardId}/user-consensus")
    public UserConsensus getUserConsensus(@PathVariable UUID cardId) {
        return reviewService.getUserConsensus(cardId);
    }

    @GetMapping("card/{cardId}/chart")
    public ScoreChartResponseDTO getScoreChart(@PathVariable UUID cardId) {
        return reviewService.getScoreChart(cardId);
    }

    @GetMapping("card/{cardId}/user/{userId}")
    public Optional<ReviewModelDTO> getUserReview(@PathVariable UUID cardId, @PathVariable UUID userId) {
        return reviewService.getUserReview(cardId, userId);
    }

    @PostMapping
    public void addReview(@RequestBody AddReviewModel reviewModel) {
        reviewService.addReview(reviewModel);
    }

    @PutMapping
    public void updateReview(@RequestBody AddReviewModel reviewModel) {
        reviewService.updateReview(reviewModel);
    }

    @PatchMapping("{reviewId}")
    public void censorUnCensorReview(@PathVariable UUID reviewId) {
        reviewService.censorUnCensorReview(reviewId);
    }
}

