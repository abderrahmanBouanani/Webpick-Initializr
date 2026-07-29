package com.webpick.initializr.generation.infrastructure.mapper;

import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.infrastructure.persistance.HistoryEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring", imports = LocalDateTime.class)
public interface HistoryEntityMapper {

    @Mapping(target = "id", ignore = true) // L'ID technique DB est auto-généré
    @Mapping(target = "contextId", source = "id")
    @Mapping(target = "generatedAt", expression = "java(LocalDateTime.now())")
    HistoryEntity toEntity(GenerationContext context);
}
