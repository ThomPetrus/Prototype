package com.motive.prototype.domain.service;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Slf4j
@Service
public class FileDownloadService {

    public Flux<Resource> getAllFilesInLocalDir() throws IOException {
        AtomicReference<Path> foundFile = null;

        Path dirPath = Paths.get("/resources/temp-assets");
        List<Resource> resources = new ArrayList<>();

        Files.list(dirPath).forEach(file -> {
            try {
                resources.add(new UrlResource(file.toUri()));
            } catch (MalformedURLException e) {
                log.error("Incorrect URL for asset file", e);
            }
        });

        return Flux.fromIterable(resources);
    }
}
