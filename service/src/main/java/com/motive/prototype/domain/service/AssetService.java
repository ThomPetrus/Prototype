package com.motive.prototype.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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
}
