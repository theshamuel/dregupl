package com.theshamuel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.theshamuel.entity.ImageConfig;
import com.theshamuel.entity.ImageManifest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;

@Service
public class SyncService {
    private static Logger logger = LoggerFactory.getLogger(SyncService.class);

    private final ObjectMapper mapper;

    private final RestTemplate restTemplateRegFrom;
    private final RestTemplate restTemplateRegTo;
    private final String REG_FROM_URL = "http://localhost:5000/v2";
    private final String REG_TO_URL = "http://localhost:5002/v2";
    private final String MANIFEST_MEDIA_TYPE = "application/vnd.docker.distribution.manifest.v2+json";
    private final String TEST_IMAGE_NAME_TAG = "theshamuel/ed-toolbox:latest";


    public SyncService(ObjectMapper mapper, RestTemplate restTemplateRegFrom, RestTemplate restTemplateRegTo) {
        this.mapper = mapper;
        this.restTemplateRegFrom = restTemplateRegFrom;
        this.restTemplateRegTo = restTemplateRegTo;
    }

    public String sync() {
        try {
            pushImage(TEST_IMAGE_NAME_TAG, getManifest(TEST_IMAGE_NAME_TAG), pullImage(TEST_IMAGE_NAME_TAG));
            return syncChunks(TEST_IMAGE_NAME_TAG);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "invalid request";
    }

    private void pushImage(String image, ImageManifest manifest, Path[] paths) {

        for (int i = paths.length - 1; i > -1; i--) {
            URI location = pushInit(image);
            if (location != null) {
                int index = 0;
                boolean last = false;
                long fileSize = 0;
                try (FileChannel fileChannel = FileChannel.open(paths[i])) {
                    fileSize = fileChannel.size();
                    logger.info("paths {}, size = {}", paths[i], fileSize);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try (FileChannel fis = new FileInputStream(paths[i].toFile()).getChannel()) {
                    int offset;
                    ByteBuffer buf = ByteBuffer.allocate(20971520);
                    while (true) {
                        int x = fis.read(buf);
                        offset = index + x;
                        RequestEntity r;
                        if (offset == fileSize) {
                            last = true;
                        }
                        String contentRange = HttpRange.toString(Collections.singletonList(HttpRange.createByteRange(index, offset)));
                        if (last) {
                            byte[] k = Arrays.copyOf(buf.array(), x);
                            r = RequestEntity.
                                    put(new URI(location.toString() + "&digest=sha256:" + paths[i].toFile().getName()))
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(buf.array().length))
                                    .header(HttpHeaders.CONTENT_RANGE, contentRange)
                                    .body(k);
                        } else {
                            r = RequestEntity.
                                    patch(location)
                                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                                    .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(buf.array().length))
                                    .header(HttpHeaders.CONTENT_RANGE, contentRange)
                                    .body(buf.array());
                        }
                        buf.clear();
                        index = offset;
                        ResponseEntity resp = restTemplateRegTo.exchange(r, Void.class);
                        if (last) {
                            break;
                        }
                        location = new URI(resp.getHeaders().get("Location").iterator().next());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

            }
        }
        pushManifest(image, manifest);
    }

    private void pushManifest(String image, ImageManifest manifest) {
        logger.info("PUSH_MANIFEST");
        try {
            RequestEntity r = RequestEntity.
                    put(new URI(REG_TO_URL + "/" + image.split(":")[0] + "/manifests/" + image.split(":")[1]))
                    .header(HttpHeaders.CONTENT_TYPE, manifest.getMediaType())
                    .body(mapper.writeValueAsString(manifest));
            restTemplateRegTo.exchange(r, Void.class);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    private URI pushInit(String image) {
        try {
            RequestEntity r = RequestEntity.
                    post(new URI(REG_TO_URL + "/" + image.split(":")[0] + "/blobs/uploads/"))
                    .header(HttpHeaders.CONTENT_LENGTH, "0")
                    .build();
            ResponseEntity responseEntity = restTemplateRegTo.exchange(r, Void.class);
            return new URI(responseEntity.getHeaders().get("Location").iterator().next());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Path[] pullImage() {
        return pullImage(TEST_IMAGE_NAME_TAG);
    }

    public Path[] pullImage(String image) {
        Path[] result = null;
        try {
            ImageManifest manifest = getManifest(TEST_IMAGE_NAME_TAG);
            result = new Path[manifest.getLayers().length + 1];
            result[0] = pullConfig(image, manifest);
            int i = 1;
            for (Path path : pullLayers(image, manifest)) {
                result[i] = path;
                i++;
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return result;
    }

    private Path pullConfig(String image, ImageManifest manifest) {
        Path path = Paths.get("storage/" + manifest.getConfig().getDigest().replace("sha256:", ""));
        try (BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"))) {
            RequestEntity r = RequestEntity.
                    get(new URI(REG_FROM_URL + "/" + image.split(":")[0] + "/blobs/" + manifest.getConfig().getDigest()))
                    .header("Accept", MediaType.APPLICATION_JSON_VALUE)
                    .build();

            String result = restTemplateRegFrom.exchange(r, String.class).getBody();

            writer.write(result);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }

    private Path[] pullLayers(String image, ImageManifest manifest) {
        Path[] result = new Path[manifest.getLayers().length];
        restTemplateRegFrom.getMessageConverters().add(
                new ByteArrayHttpMessageConverter());
        int i = 0;
        for (ImageConfig ic : manifest.getLayers()) {
            Path path = Paths.get("storage/" + ic.getDigest().replace("sha256:", ""));
            try {
                RequestEntity r = RequestEntity.
                        get(new URI(REG_FROM_URL + "/" + image.split(":")[0] + "/blobs/" + ic.getDigest()))
                        .build();
                Files.write(path, restTemplateRegFrom.exchange(r, byte[].class).getBody());
                result[i] = path;
                i++;
            } catch (URISyntaxException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    public String syncChunks(String image) throws URISyntaxException, JsonProcessingException {
        return mapper.writeValueAsString(getManifest(image));
    }

    public ImageManifest getManifest(String image) throws URISyntaxException {
        RequestEntity r = RequestEntity.
                get(new URI(REG_FROM_URL + "/" + image.split(":")[0] + "/manifests/" + image.split(":")[1]))
                .header("Accept", MANIFEST_MEDIA_TYPE)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        return restTemplateRegFrom.exchange(r, ImageManifest.class).getBody();
    }

    public void deleteImage(String image, String tag) {
        //stub
    }
}
