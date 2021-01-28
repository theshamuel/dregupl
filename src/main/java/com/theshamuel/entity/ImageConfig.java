package com.theshamuel.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ImageConfig implements Serializable {

    private String mediaType;
    private String digest;
    private long size;

    public ImageConfig() {
    }

    public ImageConfig(String mediaType, String digest, long size) {
        this.mediaType = mediaType;
        this.digest = digest;
        this.size = size;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDigest() {
        return digest;
    }

    public void setDigest(String digest) {
        this.digest = digest;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "ImageConfig{" +
                "mediaType='" + mediaType + '\'' +
                ", digest='" + digest + '\'' +
                ", size=" + size +
                '}';
    }
}
