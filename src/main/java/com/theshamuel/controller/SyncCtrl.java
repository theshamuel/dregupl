package com.theshamuel.controller;


import com.theshamuel.service.SyncService;
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
public class SyncCtrl {

    private static Logger logger = LoggerFactory.getLogger(SyncCtrl.class);

    public final SyncService syncService;

    public SyncCtrl(SyncService syncService) {
        this.syncService = syncService;
    }

    @PostMapping(value = "/sync", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity syncImages() {
        Instant start = Instant.now();
        Instant finish = Instant.now();
        String result = syncService.sync();
        logger.debug("Elapsed time getClientInfo: {}", Duration.between(start, finish).toMillis());
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping(value = "/ping")
    public ResponseEntity<String> ping() {
        return new ResponseEntity("pong", HttpStatus.OK);
    }

}
