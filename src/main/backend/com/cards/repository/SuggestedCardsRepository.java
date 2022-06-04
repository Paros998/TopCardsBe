package com.cards.repository;

import com.cards.entity.SuggestedCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface SuggestedCardsRepository extends JpaRepository<SuggestedCards, UUID>, JpaSpecificationExecutor<SuggestedCards> {
}