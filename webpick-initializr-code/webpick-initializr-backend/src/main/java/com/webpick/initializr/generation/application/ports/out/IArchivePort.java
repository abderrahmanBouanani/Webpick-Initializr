package com.webpick.initializr.generation.application.ports.out;

import java.nio.file.Path;

public interface IArchivePort {
    byte[] compressToZip(Path sourceDirectory);
}
