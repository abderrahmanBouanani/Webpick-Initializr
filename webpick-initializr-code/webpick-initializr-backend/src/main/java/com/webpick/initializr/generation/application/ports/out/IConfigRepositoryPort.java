package com.webpick.initializr.generation.application.ports.out;

import com.webpick.initializr.generation.domain.entities.GenerationContext;

public interface IConfigRepositoryPort {
    void saveHistory(GenerationContext context);

}
