package com.stevenzcaho.localcatfindingsystem.controller;

import com.stevenzcaho.localcatfindingsystem.service.TopHitsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("topHits")
public class TopHitsController {

    @Autowired
    private TopHitsService service;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String,Double>> getTopHits(){
        return this.service.top3Products2()
                .repeatWhen(l -> Flux.interval(Duration.ofSeconds(3)));
    }
}
