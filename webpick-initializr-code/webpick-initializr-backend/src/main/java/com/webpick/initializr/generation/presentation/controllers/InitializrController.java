package com.webpick.initializr.generation.presentation.controllers;



import com.webpick.initializr.generation.application.ports.in.IGenerateProjectUseCase;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import com.webpick.initializr.generation.presentation.dto.ProjectRequestDTO;
import com.webpick.initializr.generation.presentation.mappers.ProjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/v1/projects")
@Tag(name = "Project Generation", description = "Endpoints for project bootstrapping, configuration, and archive delivery")
public class InitializrController {

    // Dependency injection of the use case and mapper
    private final IGenerateProjectUseCase useCase;
    private final ProjectMapper mapper;

    public InitializrController(IGenerateProjectUseCase useCase, ProjectMapper mapper) {
        this.useCase = useCase;
        this.mapper = mapper;
    }

    @PostMapping(value = "/generate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = "application/zip")
    @Operation(
            summary = "Generate project starter archive",
            description = "Processes the requested project configuration, renders FreeMarker code templates, packages them into a ZIP archive, and optionally initiates remote Git export."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Project ZIP archive successfully generated and returned as binary stream",
                    content = @Content(mediaType = "application/zip")
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid configuration parameters",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server or template rendering error",
                    content = @Content(mediaType = "text/plain", schema = @Schema(implementation = String.class))
            )
    })
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