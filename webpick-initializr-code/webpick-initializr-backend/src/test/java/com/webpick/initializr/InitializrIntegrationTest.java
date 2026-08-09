package com.webpick.initializr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.ByteArrayInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class InitializrIntegrationTest {

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    void testEndToEndSpringProjectGeneration() throws Exception {
        String requestBody = "{\n" +
                "  \"projectName\": \"test-spring-app\",\n" +
                "  \"basePackage\": \"com.webpick.testapp\",\n" +
                "  \"includeGit\": false,\n" +
                "  \"backend\": \"SPRING\",\n" +
                "  \"frontend\": \"NONE\",\n" +
                "  \"db\": \"POSTGRES\",\n" +
                "  \"deps\": [\"jpa\", \"lombok\"],\n" +
                "  \"devops\": [\"docker\", \"jenkins\"],\n" +
                "  \"metadata\": {\n" +
                "    \"buildTool\": \"Maven\",\n" +
                "    \"javaVersion\": \"17\",\n" +
                "    \"groupId\": \"com.webpick\",\n" +
                "    \"artifactId\": \"testapp\"\n" +
                "  }\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/v1/projects/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        byte[] zipBytes = result.getResponse().getContentAsByteArray();
        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);

        // Verify it is a valid zip and check expected files
        boolean foundApplicationJava = false;
        boolean foundPomXml = false;
        boolean foundDockerCompose = false;
        boolean foundJenkinsfile = false;
        boolean foundGitignore = false;
        boolean foundDockerfile = false;

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith("Application.java")) {
                    foundApplicationJava = true;
                } else if (name.endsWith("pom.xml")) {
                    foundPomXml = true;
                } else if (name.endsWith("docker-compose.yml")) {
                    foundDockerCompose = true;
                } else if (name.endsWith("Jenkinsfile")) {
                    foundJenkinsfile = true;
                } else if (name.endsWith(".gitignore")) {
                    foundGitignore = true;
                } else if (name.endsWith("Dockerfile")) {
                    foundDockerfile = true;
                }
                zipInputStream.closeEntry();
            }
        }

        assertTrue(foundApplicationJava, "Application.java should be generated");
        assertTrue(foundPomXml, "pom.xml should be generated");
        assertTrue(foundDockerCompose, "docker-compose.yml should be generated");
        assertTrue(foundJenkinsfile, "Jenkinsfile should be generated");
        assertTrue(foundGitignore, ".gitignore should be generated");
        assertTrue(foundDockerfile, "Dockerfile should be generated");
    }

    @Test
    void testEndToEndDjangoProjectGeneration() throws Exception {
        String requestBody = "{\n" +
                "  \"projectName\": \"test-django-app\",\n" +
                "  \"basePackage\": \"com.webpick.testapp\",\n" +
                "  \"includeGit\": false,\n" +
                "  \"backend\": \"DJANGO\",\n" +
                "  \"frontend\": \"NONE\",\n" +
                "  \"db\": \"POSTGRES\",\n" +
                "  \"deps\": [],\n" +
                "  \"devops\": [\"docker\"],\n" +
                "  \"metadata\": {\n" +
                "    \"buildTool\": \"pip\",\n" +
                "    \"groupId\": \"com.webpick\",\n" +
                "    \"artifactId\": \"testapp\"\n" +
                "  }\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/v1/projects/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        byte[] zipBytes = result.getResponse().getContentAsByteArray();
        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);

        boolean foundManagePy = false;
        boolean foundSettingsPy = false;
        boolean foundRequirementsTxt = false;
        boolean foundDockerCompose = false;
        boolean foundGitignore = false;
        boolean foundInitPy = false;
        boolean foundUrlsPy = false;
        boolean foundWsgiPy = false;
        boolean foundAsgiPy = false;
        boolean foundDockerfile = false;

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith("manage.py")) {
                    foundManagePy = true;
                } else if (name.endsWith("settings.py")) {
                    foundSettingsPy = true;
                } else if (name.endsWith("requirements.txt")) {
                    foundRequirementsTxt = true;
                } else if (name.endsWith("docker-compose.yml")) {
                    foundDockerCompose = true;
                } else if (name.endsWith(".gitignore")) {
                    foundGitignore = true;
                } else if (name.endsWith("__init__.py")) {
                    foundInitPy = true;
                } else if (name.endsWith("urls.py")) {
                    foundUrlsPy = true;
                } else if (name.endsWith("wsgi.py")) {
                    foundWsgiPy = true;
                } else if (name.endsWith("asgi.py")) {
                    foundAsgiPy = true;
                } else if (name.endsWith("Dockerfile")) {
                    foundDockerfile = true;
                }
                zipInputStream.closeEntry();
            }
        }

        assertTrue(foundManagePy, "manage.py should be generated");
        assertTrue(foundSettingsPy, "settings.py should be generated");
        assertTrue(foundRequirementsTxt, "requirements.txt should be generated");
        assertTrue(foundDockerCompose, "docker-compose.yml should be generated");
        assertTrue(foundGitignore, ".gitignore should be generated");
        assertTrue(foundInitPy, "__init__.py should be generated");
        assertTrue(foundUrlsPy, "urls.py should be generated");
        assertTrue(foundWsgiPy, "wsgi.py should be generated");
        assertTrue(foundAsgiPy, "asgi.py should be generated");
        assertTrue(foundDockerfile, "Dockerfile should be generated");
    }

    @Test
    void testEndToEndExpressProjectGeneration() throws Exception {
        String requestBody = "{\n" +
                "  \"projectName\": \"test-express-app\",\n" +
                "  \"basePackage\": \"com.webpick.testapp\",\n" +
                "  \"includeGit\": false,\n" +
                "  \"backend\": \"EXPRESS\",\n" +
                "  \"frontend\": \"NONE\",\n" +
                "  \"db\": \"MONGODB\",\n" +
                "  \"deps\": [],\n" +
                "  \"devops\": [\"docker\"],\n" +
                "  \"metadata\": {\n" +
                "    \"buildTool\": \"npm\",\n" +
                "    \"groupId\": \"com.webpick\",\n" +
                "    \"artifactId\": \"testapp\"\n" +
                "  }\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/v1/projects/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        byte[] zipBytes = result.getResponse().getContentAsByteArray();
        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);

        boolean foundPackageJson = false;
        boolean foundIndexJs = false;
        boolean foundDockerCompose = false;
        boolean foundGitignore = false;
        boolean foundDockerfile = false;

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith("package.json")) {
                    foundPackageJson = true;
                } else if (name.endsWith("index.js")) {
                    foundIndexJs = true;
                } else if (name.endsWith("docker-compose.yml")) {
                    foundDockerCompose = true;
                } else if (name.endsWith(".gitignore")) {
                    foundGitignore = true;
                } else if (name.endsWith("Dockerfile")) {
                    foundDockerfile = true;
                }
                zipInputStream.closeEntry();
            }
        }

        assertTrue(foundPackageJson, "package.json should be generated");
        assertTrue(foundIndexJs, "index.js should be generated");
        assertTrue(foundDockerCompose, "docker-compose.yml should be generated");
        assertTrue(foundGitignore, ".gitignore should be generated");
        assertTrue(foundDockerfile, "Dockerfile should be generated");
    }

    @Test
    void testEndToEndSymfonyProjectGeneration() throws Exception {
        String requestBody = "{\n" +
                "  \"projectName\": \"test-symfony-app\",\n" +
                "  \"basePackage\": \"com.webpick.testapp\",\n" +
                "  \"includeGit\": false,\n" +
                "  \"backend\": \"SYMFONY\",\n" +
                "  \"frontend\": \"NONE\",\n" +
                "  \"db\": \"POSTGRES\",\n" +
                "  \"deps\": [],\n" +
                "  \"devops\": [\"docker\"],\n" +
                "  \"metadata\": {\n" +
                "    \"buildTool\": \"composer\",\n" +
                "    \"groupId\": \"com.webpick\",\n" +
                "    \"artifactId\": \"testapp\"\n" +
                "  }\n" +
                "}";

        MvcResult result = mockMvc.perform(post("/api/v1/projects/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody))
                .andDo(org.springframework.test.web.servlet.result.MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        byte[] zipBytes = result.getResponse().getContentAsByteArray();
        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);

        boolean foundComposerJson = false;
        boolean foundIndexPhp = false;
        boolean foundEnv = false;
        boolean foundDockerCompose = false;
        boolean foundGitignore = false;
        boolean foundKernelPhp = false;
        boolean foundBundlesPhp = false;
        boolean foundRoutesYaml = false;
        boolean foundDockerfile = false;

        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zipInputStream.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.endsWith("composer.json")) {
                    foundComposerJson = true;
                } else if (name.endsWith("index.php")) {
                    foundIndexPhp = true;
                } else if (name.endsWith(".env")) {
                    foundEnv = true;
                } else if (name.endsWith("docker-compose.yml")) {
                    foundDockerCompose = true;
                } else if (name.endsWith(".gitignore")) {
                    foundGitignore = true;
                } else if (name.endsWith("Kernel.php")) {
                    foundKernelPhp = true;
                } else if (name.endsWith("bundles.php")) {
                    foundBundlesPhp = true;
                } else if (name.endsWith("routes.yaml")) {
                    foundRoutesYaml = true;
                } else if (name.endsWith("Dockerfile")) {
                    foundDockerfile = true;
                }
                zipInputStream.closeEntry();
            }
        }

        assertTrue(foundComposerJson, "composer.json should be generated");
        assertTrue(foundIndexPhp, "index.php should be generated");
        assertTrue(foundEnv, ".env should be generated");
        assertTrue(foundDockerCompose, "docker-compose.yml should be generated");
        assertTrue(foundGitignore, ".gitignore should be generated");
        assertTrue(foundKernelPhp, "Kernel.php should be generated");
        assertTrue(foundBundlesPhp, "bundles.php should be generated");
        assertTrue(foundRoutesYaml, "routes.yaml should be generated");
        assertTrue(foundDockerfile, "Dockerfile should be generated");
    }
}

