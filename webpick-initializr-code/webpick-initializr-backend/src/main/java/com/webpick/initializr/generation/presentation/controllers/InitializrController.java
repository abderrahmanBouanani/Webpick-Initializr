package com.webpick.initializr.generation.presentation.controllers;



import com.webpick.initializr.generation.application.ports.in.IGenerateProjectUseCase;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.presentation.dto.ProjectRequestDTO;
import com.webpick.initializr.generation.presentation.mappers.ProjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//we should writh all the comments in english and briefly
@RestController
@RequestMapping("/api/v1/projects")
public class InitializrController {

    // Dependency injection of the use case and mapper
    private final IGenerateProjectUseCase useCase;
    private final ProjectMapper mapper;

    public InitializrController(IGenerateProjectUseCase useCase, ProjectMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateProject(@RequestBody ProjectRequestDTO request) {
        try {
            // 1. Mapping the incoming request DTO to the domain GenerationContext
            GenerationContext context = mapper.toGenerationContext(request);

            // 2. Execute the use case to generate the project and get the zip archive
            byte[] zipArchive = useCase.execute(context);

            // 3. Prepare the filename for the zip file
            String filename = context.getProjectName() + ".zip";

            // 4. Set the response headers for file download
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("application/zip"));
            headers.setContentDispositionFormData("attachment", filename);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

            return new ResponseEntity<>(zipArchive, headers, HttpStatus.OK);

        } catch (IllegalArgumentException e) {
            // Handle bad request errors
            return ResponseEntity.badRequest().body(e.getMessage().getBytes());
        } catch (Exception e) {
            // Handle internal server errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(("Erreur lors de la génération du projet : " + e.getMessage()).getBytes());
        }
    }
}