package com.webpick.initializr.git.out;

import com.webpick.initializr.generation.domain.entities.GenerationContext;

public interface IGitRemotePort {
    void pushToRemote(GenerationContext context, byte[] zipContent);
}
