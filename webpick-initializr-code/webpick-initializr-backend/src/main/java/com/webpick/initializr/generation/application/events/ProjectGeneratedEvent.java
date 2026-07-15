package com.webpick.initializr.generation.application.events;

import com.webpick.initializr.generation.domain.entities.GenerationContext;

public class ProjectGeneratedEvent {
    private GenerationContext context;
    private byte[] zipContent;

    public ProjectGeneratedEvent(GenerationContext context, byte[] zipContent) {
        this.context = context;
        this.zipContent = zipContent;
    }

    public GenerationContext getContext() {
        return context;
    }

    public byte[] getZipContent() {
        return zipContent;
    }
}
