package com.webpick.initializr.git.infrastructure;

import com.webpick.initializr.generation.domain.entities.BackendFramework;
import com.webpick.initializr.generation.domain.entities.GenerationContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GithubApiAdapterTest {

    private RestTemplate restTemplate;
    private GithubApiAdapter adapter;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        adapter = new GithubApiAdapter(restTemplate);
    }

    @Test
    void testPushToRemoteSuccess() throws Exception {
        // Prepare context
        GenerationContext context = new GenerationContext.Builder()
                .projectName("my-project")
                .basePackage("com.example")
                .backendFramework(BackendFramework.SPRING)
                .includeGit(true)
                .gitToken("test-token")
                .build();

        // Create mock zip content with a file
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ZipOutputStream zos = new ZipOutputStream(bos)) {
            zos.putNextEntry(new ZipEntry("README.md"));
            zos.write("Hello World".getBytes());
            zos.closeEntry();
        }
        byte[] zipBytes = bos.toByteArray();

        // Mock GitHub User API
        Map<String, String> userResponse = new HashMap<>();
        userResponse.put("login", "octocat");
        ResponseEntity<Map> userEntity = new ResponseEntity<>(userResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                eq("https://api.github.com/user"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(userEntity);

        // Mock Repo creation API
        ResponseEntity<Map> repoEntity = new ResponseEntity<>(new HashMap<>(), HttpStatus.CREATED);
        when(restTemplate.postForEntity(
                eq("https://api.github.com/user/repos"),
                any(HttpEntity.class),
                eq(Map.class)
        )).thenReturn(repoEntity);

        // Run
        adapter.pushToRemote(context, zipBytes);

        // Verify calls
        verify(restTemplate, times(1)).exchange(
                eq("https://api.github.com/user"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(Map.class)
        );
        verify(restTemplate, times(1)).postForEntity(
                eq("https://api.github.com/user/repos"),
                any(HttpEntity.class),
                eq(Map.class)
        );
        verify(restTemplate, times(1)).put(
                eq("https://api.github.com/repos/octocat/my-project/contents/README.md"),
                any(HttpEntity.class)
        );
    }

    @Test
    void testPushToRemoteMissingToken() {
        GenerationContext context = new GenerationContext.Builder()
                .projectName("my-project")
                .basePackage("com.example")
                .backendFramework(BackendFramework.SPRING)
                .includeGit(false)
                .gitToken("")
                .build();

        assertThrows(IllegalArgumentException.class, () -> adapter.pushToRemote(context, new byte[0]));
    }
}
