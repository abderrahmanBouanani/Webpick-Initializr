package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.infrastructure.mapper.HistoryEntityMapper;
import com.webpick.initializr.generation.infrastructure.persistance.HistoryEntity;
import com.webpick.initializr.generation.infrastructure.repository.HistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class PostgresConfigAdapterTest {

    private HistoryRepository repository;
    private HistoryEntityMapper mapper;
    private PostgresConfigAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(HistoryRepository.class);
        mapper = mock(HistoryEntityMapper.class);
        adapter = new PostgresConfigAdapter(repository, mapper);
    }

    @Test
    void testSaveHistorySuccess() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("my-project")
                .basePackage("com.example")
                .backendFramework(BackendFramework.SPRING)
                .includeGit(false)
                .build();

        HistoryEntity entity = new HistoryEntity();
        when(mapper.toEntity(context)).thenReturn(entity);

        adapter.saveHistory(context);

        verify(mapper, times(1)).toEntity(context);
        verify(repository, times(1)).save(entity);
    }
}
