package com.webpick.initializr;

import com.webpick.initializr.generation.application.ports.in.IGenerateProjectUseCase;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.presentation.controllers.InitializrController;
import com.webpick.initializr.generation.presentation.mappers.ProjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

class InitializrControllerTest {

    private MockMvc mockMvc;
    private IGenerateProjectUseCase useCase;
    private ProjectMapper mapper;

    @BeforeEach
    void setUp() {
        useCase = Mockito.mock(IGenerateProjectUseCase.class);
        mapper = new ProjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(new InitializrController(useCase, mapper)).build();
    }

    @Test
    void testGenerateProjectSuccess() throws Exception {
        byte[] mockZip = "dummy-zip-content".getBytes();
        when(useCase.execute(any(GenerationContext.class))).thenReturn(mockZip);

        String requestBody = "{\n" +
                "  \"projectName\": \"demo-project\",\n" +
                "  \"basePackage\": \"com.example.demo\",\n" +
                "  \"includeGit\": false,\n" +
                "  \"backend\": \"SPRING\",\n" +
                "  \"frontend\": \"NONE\",\n" +
                "  \"db\": \"POSTGRES\",\n" +
                "  \"deps\": [\"jpa\", \"lombok\"],\n" +
                "  \"devops\": [\"docker\"],\n" +
                "  \"metadata\": {\n" +
                "    \"buildTool\": \"Maven\",\n" +
                "    \"javaVersion\": \"17\",\n" +
                "    \"groupId\": \"com.example\",\n" +
                "    \"artifactId\": \"demo\"\n" +
                "  }\n" +
                "}";

        mockMvc.perform(post("/api/v1/projects/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "form-data; name=\"attachment\"; filename=\"demo-project.zip\""))
                .andExpect(content().contentType("application/zip"));
    }

    @Test
    void testGenerateProjectInvalidPackage() throws Exception {
        String requestBody = "{\n" +
                "  \"projectName\": \"demo-project\",\n" +
                "  \"basePackage\": \"invalid-package-name\",\n" +
                "  \"backend\": \"SPRING\"\n" +
                "}";

        mockMvc.perform(post("/api/v1/projects/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andExpect(status().isBadRequest());
    }
}
