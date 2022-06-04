package com.cards.serviceImplementation;

import com.cards.dto.request.AddHistoryDTO;
import com.cards.dto.request.AddReviewModel;
import com.cards.dto.request.PageRequestDTO;
import com.cards.dto.response.PageResponse;
import com.cards.dto.response.ReviewModelDTO;
import com.cards.dto.response.ScoreChartResponseDTO;
import com.cards.dto.response.UserConsensus;
import com.cards.entity.GraphicCard;
import com.cards.entity.Review;
import com.cards.entity.User;
import com.cards.enums.Action;
import com.cards.repository.ReviewRepository;
import com.cards.serviceInterface.ICardsService;
import com.cards.serviceInterface.IHistoryService;
import com.cards.serviceInterface.IReviewService;
import com.cards.serviceInterface.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class ReviewService implements IReviewService {
    private final ReviewRepository reviewRepository;
    private final ICardsService cardsService;
    private final IUserService userService;
    private final IHistoryService historyService;

    public Review getReview(UUID reviewId) {
        return reviewRepository.findById(reviewId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Review with id %s not found in database", reviewId))
        );
    }

    public PageResponse<ReviewModelDTO> getCardReviews(UUID cardId, PageRequestDTO pageRequestDTO) {
        return new PageResponse<>(
                reviewRepository.findAllByCardCardId(cardId, pageRequestDTO.getRequest(Review.class))
                        .map(ReviewModelDTO::convertFromEntity)

        );
    }

    public UserConsensus getUserConsensus(UUID cardId) {

        List<Review> reviews = reviewRepository.findAllByCardCardId(cardId);

        AtomicReference<Double> score = new AtomicReference<>(0d);

        reviews.forEach(review -> score.set(score.get() + review.getScore()));

        score.set(score.get() / reviews.size());

        return new UserConsensus(
                score.get(),
                reviews.size()
        );
    }

    @Transactional(rollbackOn = ResponseStatusException.class)
    public void addReview(AddReviewModel reviewModel) {

        User user = userService.getUser(reviewModel.getUserId());
        GraphicCard card = cardsService.getCard(reviewModel.getCardId());

        if (reviewRepository.existsByUserAndCard(user, card))
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    String.format(
                            "Review with cardId %s and userId %s already exists",
                            reviewModel.getCardId(),
                            reviewModel.getUserId()
                    )
            );

        Review review = new Review(
                reviewModel.getOpinion(),
                reviewModel.getCensored(),
                reviewModel.getScore(),
                LocalDateTime.now(),
                user,
                card
        );

        historyService.addHistory(new AddHistoryDTO(
                Action.opinion.name(),
                card.getCardId().toString(),
                user.getUserId().toString()
        ));

        reviewRepository.save(review);

    }

    @Transactional(rollbackOn = ResponseStatusException.class)
    public void updateReview(AddReviewModel reviewModel) {

        Review review = getReview(reviewModel.getId());

        review.setReviewDate(LocalDateTime.now());

        review.setOpinion(reviewModel.getOpinion());

        review.setScore(reviewModel.getScore());

        historyService.addHistory(new AddHistoryDTO(
                Action.opinion.name(),
                reviewModel.getCardId().toString(),
                reviewModel.getUserId().toString()
        ));

        reviewRepository.save(review);

    }

    public void censorUnCensorReview(UUID reviewId) {

        Review review = getReview(reviewId);

        review.setIsCensored(!review.getIsCensored());

        reviewRepository.save(review);

    }

    public ScoreChartResponseDTO getScoreChart(UUID cardId) {

        List<Review> reviews = reviewRepository.findAllByCardCardId(cardId);

        Integer[] count = {0, 0, 0, 0, 0, 0};

        reviews.forEach(review -> {
            count[review.getScore()] += 1;
        });

        return new ScoreChartResponseDTO(
                reviews.size(),
                count
        );
    }

    public Optional<ReviewModelDTO> getUserReview(UUID cardId, UUID userId) {
        return reviewRepository.findByUserUserIdAndCardCardId(userId, cardId).map(ReviewModelDTO::convertFromEntity);
    }
}
