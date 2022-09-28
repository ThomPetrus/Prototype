package com.motive.prototype.domain.service;

import com.motive.prototype.domain.model.AssetMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
public class AssetService {

    // TODO: replace with actual repo like dynamoDB to store assets
    private FileDownloadService fileDownloadService;

    @Autowired
    public AssetService(FileDownloadService fileDownloadService) {
        this.fileDownloadService = fileDownloadService;
    }

    public Flux<Resource> getAllAssets() {
        try {
            return fileDownloadService.getAllFilesInLocalDir();
        } catch (IOException e) {
            log.error("IO Error retrieving assets", e);
            return Flux.empty();
        }
    }

    public Mono<Resource> getAsset(String fileName) {
        try {
            return fileDownloadService.getFile(fileName);
        } catch (IOException e) {
            log.error("IO Error retrieving asset", e);
            return Mono.empty();
        }
    }

    public Flux<AssetMetadata> getAllFileNames() {
        try {
            return fileDownloadService.getAllFileNames();
        } catch (IOException e) {
            log.error("IO Error retrieving asset filenames", e);
            return Flux.empty();
        }
    }

    public Mono<ResponseEntity<Resource>> getAssetByFileName2(String fileName) {
        return fileDownloadService.getFile2(fileName);
    }
}
