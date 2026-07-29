package com.webpick.initializr.generation.infrastructure.repository;

import com.webpick.initializr.generation.infrastructure.persistance.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {
}
