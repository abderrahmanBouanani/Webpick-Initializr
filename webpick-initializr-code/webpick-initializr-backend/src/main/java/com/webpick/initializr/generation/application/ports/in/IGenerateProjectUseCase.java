package com.webpick.initializr.generation.application.ports.in;

import com.webpick.initializr.generation.domain.entities.GenerationContext;

public interface IGenerateProjectUseCase {
    public byte[] execute(GenerationContext context);
}
