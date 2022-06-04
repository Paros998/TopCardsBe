package com.cards.serviceInterface;

import com.cards.dto.request.AddReviewModel;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.PageResponse;
import com.cards.dto.response.ReviewModelDTO;
import com.cards.dto.response.ScoreChartResponseDTO;
import com.cards.dto.response.UserConsensus;
import com.cards.entity.Review;

import java.util.Optional;
import java.util.UUID;

public interface IReviewService {

    Review getReview(UUID reviewId);

    PageResponse<ReviewModelDTO> getCardReviews(UUID cardId, PageRequestDTO pageRequestDTO);

    UserConsensus getUserConsensus(UUID cardId);

    void addReview(AddReviewModel reviewModel);

    void updateReview(AddReviewModel reviewModel);

    void censorUnCensorReview(UUID reviewId);

    ScoreChartResponseDTO getScoreChart(UUID cardId);

    Optional<ReviewModelDTO> getUserReview(UUID cardId, UUID userId);
}
