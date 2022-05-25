package com.cards.repository;

import com.cards.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface HistoryRepository extends JpaRepository<History, UUID>, JpaSpecificationExecutor<History> {
}