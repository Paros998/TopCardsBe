package com.cards.repository;

import com.cards.entity.LocalOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface LocalOfferRepository extends JpaRepository<LocalOffer, UUID>, JpaSpecificationExecutor<LocalOffer> {
}