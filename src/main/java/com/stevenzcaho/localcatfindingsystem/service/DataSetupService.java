package com.stevenzcaho.localcatfindingsystem.service;

import com.stevenzcaho.localcatfindingsystem.entity.BikeSite;
import com.stevenzcaho.localcatfindingsystem.repository.BikeSiteRepository;
import com.stevenzcaho.localcatfindingsystem.util.BikeSiteUtil;
import com.stevenzcaho.localcatfindingsystem.util.EntityDtoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class DataSetupService implements CommandLineRunner {
//    private RGeoReactive<BikeSite> bikeGeo;
//    @Autowired
//    private RedissonReactiveClient client;

    @Autowired
    private BikeSiteRepository repository;

    @Autowired
    private R2dbcEntityTemplate entityTemplate;

    @Value("classpath:schema.sql")
    private Resource resource;

    @Override
    public void run(String... args) throws Exception {
//        this.bikeGeo = this.client.getGeo("bikeSites", new TypedJsonJacksonCodec(BikeSite.class));
//
//        Flux.fromIterable(BikeSiteUtil.getGatheringSites())
//                .flatMap(r -> this.bikeGeo.add(r.getLongitude(), r.getLatitude(),r).thenReturn(r))
//                .doFinally(s -> System.out.println("bikeSites added " + s))
//                .subscribe();

        String query = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        System.out.println(query);

        Mono<Void> insert = Flux.fromIterable(BikeSiteUtil.getGatheringSites())
                .map(EntityDtoUtil::toEntity)
                .collectList()
                .flatMapMany(l -> this.repository.saveAll(l))
                .then();

        this.entityTemplate.getDatabaseClient()
                .sql(query)
                .then()
                .then(insert)
                .doFinally(s -> System.out.println("data setup done " + s))
                .subscribe();

    }
}
