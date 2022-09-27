package com.motive.prototype.controller;

import com.motive.prototype.domain.service.AssetService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value="/assets")
public class AssetController {
    private AssetService assetService;

    @Autowired
    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @GetMapping("/all")
    public Flux<Resource> testRoute() {
        return this.assetService.getAllAssets();
    }
}
