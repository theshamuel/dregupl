package com.theshamuel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URISyntaxException;

@Service
public class SyncService {
    private static Logger logger = LoggerFactory.getLogger(SyncService.class);

    private final ObjectMapper mapper;
    private final DockerService dockerService;

    //TODO: refactor to param
    private final String TEST_IMAGE_NAME_TAG = "theshamuel/ed-toolbox:latest";


    public SyncService(ObjectMapper mapper, DockerService dockerService) {
        this.mapper = mapper;
        this.dockerService = dockerService;
    }

    public String sync() {
        try {
            dockerService.pushImage(TEST_IMAGE_NAME_TAG,  dockerService.getManifest(TEST_IMAGE_NAME_TAG),  dockerService.pullImage(TEST_IMAGE_NAME_TAG));
            return syncChunks(TEST_IMAGE_NAME_TAG);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "invalid request";
    }



    public String syncChunks(String image) throws URISyntaxException, JsonProcessingException {
        return mapper.writeValueAsString( dockerService.getManifest(image));
    }
}
