package com.webpick.initializr.generation.infrastructure.adapters;

import com.webpick.initializr.generation.application.ports.out.IArchivePort;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.nio.file.Path;
import java.util.zip.ZipOutputStream;

@Component
public class ZipArchiveAdapter implements IArchivePort {


    @Override
    public byte[] compressToZip(Path sourceDirectory) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(); //
            ZipOutputStream zos = new ZipOutputStream(baos);

            return new byte[0];
        }
        catch (Exception e) {
            throw new RuntimeException("Erreur lors de la compression du fichier :", e);
        }

    }
}
