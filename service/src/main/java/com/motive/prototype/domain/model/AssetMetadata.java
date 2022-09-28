package com.motive.prototype.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.core.io.Resource;

import java.nio.file.Path;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssetMetadata {
    // TODO: more metadata from Resource + whatever else
    String fileName;

    public static AssetMetadata from(Resource resource) {
        return AssetMetadata.builder()
                .fileName(resource.getFilename())
                .build();
    }
}
