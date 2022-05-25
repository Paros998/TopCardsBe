package com.cards.repository;

import com.cards.entity.GraphicCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface GraphicCardRepository extends JpaRepository<GraphicCard, UUID>, JpaSpecificationExecutor<GraphicCard> {
}