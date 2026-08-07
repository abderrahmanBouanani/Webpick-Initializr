package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.application.ports.out.IConfigRepositoryPort;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.infrastructure.mapper.HistoryEntityMapper;
import com.webpick.initializr.generation.infrastructure.persistance.HistoryEntity;
import com.webpick.initializr.generation.infrastructure.repository.HistoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

@Component
public class PostgresConfigAdapter implements IConfigRepositoryPort {
    private final HistoryRepository repository;
    private final HistoryEntityMapper mapper;

    public PostgresConfigAdapter(HistoryRepository historyRepository, HistoryEntityMapper mapper) {
        this.repository = historyRepository;
        this.mapper = mapper;
    }

    @Override
    public void saveHistory(GenerationContext context) {
        HistoryEntity entity = mapper.toEntity(context);
        repository.save(entity);
    }
}
