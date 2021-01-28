package com.theshamuel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageManifest implements Serializable {
    private int schemaVersion;
    private String mediaType;
    private ImageConfig config;
    private ImageConfig[] layers;

    public ImageManifest() {
    }

    public ImageManifest(int schemaVersion, String mediaType, ImageConfig config, ImageConfig[] layers) {
        this.schemaVersion = schemaVersion;
        this.mediaType = mediaType;
        this.config = config;
        this.layers = layers;
    }

    public int getSchemaVersion() {
        return schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion) {
        this.schemaVersion = schemaVersion;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public ImageConfig getConfig() {
        return config;
    }

    public void setConfig(ImageConfig config) {
        this.config = config;
    }

    public ImageConfig[] getLayers() {
        return layers;
    }

    public void setLayers(ImageConfig[] layers) {
        this.layers = layers;
    }

    @Override
    public String toString() {
        return "ImageManifest{" +
                "schemaVersion=" + schemaVersion +
                ", mediaType='" + mediaType + '\'' +
                ", config=" + config +
                ", layers=" + Arrays.toString(layers) +
                '}';
    }
}
