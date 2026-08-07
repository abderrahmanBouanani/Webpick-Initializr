package com.webpick.initializr.generation.infrastructure.adapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ZipArchiveAdapterTest {

    private ZipArchiveAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ZipArchiveAdapter();
    }

    @Test
    void testCompressToZipSuccess(@TempDir Path tempDir) throws IOException {
        Path file1 = tempDir.resolve("test1.txt");
        Path subDir = tempDir.resolve("subdir");
        Path file2 = subDir.resolve("test2.txt");

        Files.createDirectories(subDir);
        Files.writeString(file1, "Hello File 1");
        Files.writeString(file2, "Hello File 2");

        byte[] zipBytes = adapter.compressToZip(tempDir);

        assertNotNull(zipBytes);
        assertTrue(zipBytes.length > 0);

        boolean foundFile1 = false;
        boolean foundFile2 = false;

        try (ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(zipBytes))) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String name = entry.getName();
                if (name.equals("test1.txt")) {
                    foundFile1 = true;
                } else if (name.equals("subdir/test2.txt")) {
                    foundFile2 = true;
                }
                zis.closeEntry();
            }
        }

        assertTrue(foundFile1);
        assertTrue(foundFile2);
    }
}
