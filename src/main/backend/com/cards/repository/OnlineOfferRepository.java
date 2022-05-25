package com.cards.repository;

import com.cards.entity.OnlineOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface OnlineOfferRepository extends JpaRepository<OnlineOffer, UUID>, JpaSpecificationExecutor<OnlineOffer> {
}