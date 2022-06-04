package com.cards.repository;

import com.cards.entity.GraphicCard;
import com.cards.entity.Review;
import com.cards.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, UUID>, JpaSpecificationExecutor<Review> {

    Optional<Review> findByUserUserIdAndCardCardId(UUID userId, UUID cardId);

    Page<Review> findAllByCardCardId(UUID cardId, Pageable pageable);

    List<Review> findAllByCardCardId(UUID cardId);

    boolean existsByUserAndCard(User user, GraphicCard card);
}