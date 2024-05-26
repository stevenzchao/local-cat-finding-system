package com.stevenzcaho.localcatfindingsystem.service;

import com.stevenzcaho.localcatfindingsystem.dto.BikeSiteDto;
import com.stevenzcaho.localcatfindingsystem.service.template.CacheTemplateImplement;
import jakarta.annotation.PostConstruct;
import org.redisson.api.*;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class BikeSiteService {
    private RGeoReactive<BikeSiteDto> bikeGeo;

    private Sinks.Many<Integer> sink;

    private RMapReactive<Integer, String> nameMap;


    private RedissonReactiveClient client;


    @Autowired
    private CacheTemplateImplement cacheTemplate;

    public BikeSiteService(RedissonReactiveClient client){
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
        this.client = client;
        this.bikeGeo = client.getGeo("bikeSites",new TypedJsonJacksonCodec(BikeSiteDto.class));
        this.nameMap = client.getMap("bikeSties:idNameMap", new TypedJsonJacksonCodec(Integer.class, String.class));
    }

    public Flux<BikeSiteDto> getAllBikeSites(){
        return this.bikeGeo.iterator();
    }

    public Flux<BikeSiteDto> getBikeSitesInDistrict(String district){
        return this.cacheTemplate.getMultiple(district);
//        return this.bikeGeo.iterator().filter(g -> district.equals(g.getSarea()));
    }


    public Mono<BikeSiteDto> getSingleBikeSite(int id) {
       return this.cacheTemplate.getSingle(id)
               .flatMap(entity -> this.nameMap.putIfAbsent(id, entity.getSna()).thenReturn(entity))
               .doFinally(f -> this.addVisit(id));
    }

    @PostConstruct
    private void init(){
        this.sink.asFlux().buffer(Duration.ofSeconds(3))
                .map(l -> l.stream().collect(Collectors.groupingBy(
                                Function.identity(),
                                Collectors.counting()
                        )
                )).flatMap(this::updateBatch)
                .subscribe();
    }

    public void addVisit(int bikeSiteId){
        this.sink.tryEmitNext(bikeSiteId);
    }

    public Mono<Void> updateBatch(Map<Integer,Long> map){
        RBatchReactive batch = this.client.createBatch(BatchOptions.defaults());
        String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
        RScoredSortedSetReactive<Integer> set = this.client.getScoredSortedSet("bikeSites:visit" + format, IntegerCodec.INSTANCE);
        return Flux.fromIterable(map.entrySet())
                .flatMap(e -> set.addScore(e.getKey(),e.getValue()))
                .then(batch.execute())
                .then();
    }


}
