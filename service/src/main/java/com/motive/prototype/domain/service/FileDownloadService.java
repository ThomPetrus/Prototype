package com.motive.prototype.domain.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.motive.prototype.domain.model.AssetMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Service
public class FileDownloadService {

    @Value("${assets.dir}")
    private String assetDir;

    public Flux<Resource> getAllFilesInLocalDir() throws IOException {

        Path dirPath = Paths.get(assetDir);
        List<Resource> resources = new ArrayList<>();

        Files.list(dirPath).forEach(file -> {
            resources.add(new FileSystemResource(file));
        });

        return Flux.fromIterable(resources);
    }

    public Mono<Resource> getFile(String fileName) throws IOException {
        Path dirPath = Paths.get(assetDir);

        Optional<Path> path = Files.list(dirPath)
                .distinct()
                .filter(file -> file.getFileName().toString().contains(fileName))
                .findFirst();

        if (!path.isPresent()) {
            return Mono.empty();
        }

        return Mono.just(new FileSystemResource(path.get()));
    }

    public Flux<AssetMetadata> getAllFileNames() throws IOException {
        Path dirPath = Paths.get(assetDir);

        List<AssetMetadata> fileNames = new ArrayList<>();
        Files.list(dirPath).forEach(file -> {
            fileNames.add(AssetMetadata.from(new FileSystemResource(file)));
        });

        return Flux.fromIterable(fileNames).sort();
    }

    public Mono<ResponseEntity<Resource>> getFile2(String fileName) {
        return  Mono.fromCallable(()->{
                    String fileLocation = String.format("%s/%s", assetDir, fileName);
                    Path path = Paths.get(fileLocation).toAbsolutePath().normalize();
                    return new FileSystemResource(path);
                })
                .subscribeOn(Schedulers.boundedElastic())
                .flatMap(resource -> {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentDispositionFormData(fileName, fileName);
                    return Mono.just(ResponseEntity
                            .ok().cacheControl(CacheControl.noCache())
                            .headers(headers)
                            .body(resource));
                });
    }
}
