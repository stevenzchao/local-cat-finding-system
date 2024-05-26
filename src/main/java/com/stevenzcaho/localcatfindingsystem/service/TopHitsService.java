package com.stevenzcaho.localcatfindingsystem.service;

import org.redisson.api.RMapReactive;
import org.redisson.api.RScoredSortedSetReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.client.codec.IntegerCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TopHitsService {

    @Autowired
    private RedissonReactiveClient client;

    private RMapReactive<Integer, String> nameMap;

    public Mono<Map<Integer, Double>> top3Products() {
        String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
        RScoredSortedSetReactive<Integer> set = this.client.getScoredSortedSet("bikeSites:visit"+ format, IntegerCodec.INSTANCE);
        return set.entryRangeReversed(0,2)
                .map(listSe -> listSe.stream().collect(Collectors.toMap(
                        ScoredEntry::getValue,
                        ScoredEntry::getScore,
                        (a,b)->a,
                        LinkedHashMap::new
                )));
    }

    public Mono<Map<String, Double>> top3Products2() {
        String format = DateTimeFormatter.ofPattern("YYYYMMdd").format(LocalDate.now());
        RScoredSortedSetReactive<Integer> set = this.client.getScoredSortedSet("bikeSites:visit" + format, IntegerCodec.INSTANCE);
        nameMap = this.client.getMap("bikeSties:idNameMap", new TypedJsonJacksonCodec(Integer.class, String.class));
        return set.entryRangeReversed(0, 2)
                .flatMap(this::refactorMethod);
    }


    private Mono<Map<String, Double>> refactorMethod(Collection<ScoredEntry<Integer>> listSe){
        Map<Integer, Double> scoreMap = listSe.stream().collect(Collectors.toMap(
                ScoredEntry::getValue,
                ScoredEntry::getScore,
                (a, b) -> a,
                LinkedHashMap::new
        ));

        // Fetch the names for the IDs
        return Flux.fromIterable(scoreMap.entrySet())
                .flatMap(entry -> nameMap.get(entry.getKey())
                        .map(name  -> Map.entry(name, entry.getValue()))
                ).collectMap(Map.Entry::getKey, Map.Entry::getValue, LinkedHashMap::new);
    }
}
