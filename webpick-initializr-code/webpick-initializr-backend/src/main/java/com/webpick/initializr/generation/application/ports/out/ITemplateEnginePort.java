package com.webpick.initializr.generation.application.ports.out;

import java.util.Map;

public interface ITemplateEnginePort {
    String render(String templateName, Map<String, Object> data);
}
