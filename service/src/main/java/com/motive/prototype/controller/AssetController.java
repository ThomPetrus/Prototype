package com.motive.prototype.controller;

import com.motive.prototype.domain.model.AssetMetadata;
import com.motive.prototype.domain.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequestMapping(value="/assets")
public class AssetController {
    private AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    /**
     * Fairly certain this will not work in practice - Flux of Binary but I'll try for prototype sake
     *
     * @return
     */
    @GetMapping(value = "/all")
    public Flux<Resource> getAllAssetsInLocalDir() {
        return this.assetService.getAllAssets();
    }

    @GetMapping(value = "/{fileName}")
    public Mono<Resource> getAssetByFileName(@PathVariable String fileName) {
        log.info("Getting " + fileName);
        return this.assetService.getAsset(fileName);
    }

    @GetMapping(value = "/filenames")
    public Flux<AssetMetadata> getAllFileNames() {
        return this.assetService.getAllFileNames();
    }

    @GetMapping(value = "/asset/{fileName}", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public Mono<ResponseEntity<Resource>> loadAsset(@PathVariable("fileName") String fileName) {
        return this.assetService.getAssetByFileName2(fileName);
    }
}
