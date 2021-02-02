package com.theshamuel.controller;

import com.theshamuel.service.DockerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.Instant;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
public class DockerCtrl {

    private static Logger logger = LoggerFactory.getLogger(SyncCtrl.class);

    public final DockerService dockerService;

    public DockerCtrl(DockerService dockerService) {
        this.dockerService = dockerService;
    }

    @GetMapping(value = "/pull/images")
    public ResponseEntity pullImage() {
        Instant start = Instant.now();
        Instant finish = Instant.now();
        dockerService.pullImage();
        logger.debug("Elapsed time getClientInfo: {}", Duration.between(start, finish).toMillis());
        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping(value = "/delete/image")
    public ResponseEntity deleteImage(@RequestBody String image, @RequestBody String tag) {
        Instant start = Instant.now();
        Instant finish = Instant.now();
        dockerService.deleteImage(image, tag);
        logger.debug("Elapsed time getClientInfo: {}", Duration.between(start, finish).toMillis());
        return new ResponseEntity(HttpStatus.OK);
    }
}
