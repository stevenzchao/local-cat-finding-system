package com.stevenzcaho.localcatfindingsystem.service.template;

import com.stevenzcaho.localcatfindingsystem.dto.BikeSiteDto;
import com.stevenzcaho.localcatfindingsystem.entity.BikeSite;
import com.stevenzcaho.localcatfindingsystem.repository.BikeSiteRepository;
import com.stevenzcaho.localcatfindingsystem.util.EntityDtoUtil;
import org.redisson.api.RGeoReactive;
import org.redisson.api.RMapReactive;
import org.redisson.api.RedissonReactiveClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@Service
public class CacheTemplateImplement extends CacheTemplate<Integer, BikeSiteDto> {

    @Autowired
    private BikeSiteRepository repository;


    private RGeoReactive<BikeSiteDto> bikeGeo;

    public CacheTemplateImplement(RedissonReactiveClient client) {
        this.bikeGeo = client.getGeo("bikeSites", new TypedJsonJacksonCodec(BikeSiteDto.class));
    }

    @Override
    protected Mono<BikeSiteDto> getSingleFromCache(Integer id) {
        System.out.println("get SINGLE from cache");
        return this.bikeGeo.iterator().filter(g -> Objects.equals(id, g.getId())).next();
    }

    @Override
    protected Mono<BikeSiteDto> getSingleFromSource(Integer id) {
        System.out.println("get SINGLE from DB");
        return this.repository.findById(id).map(EntityDtoUtil::toDto).doOnNext(x -> System.out.println("did FETCH FROM DB"));
    }

    @Override
    protected Flux<BikeSiteDto> getMultipleFromCache(String sarea) {
        System.out.println("get from cache");
        return this.bikeGeo.iterator()
                .filter(g -> sarea.equals(g.getSarea()));
    }
    @Override
    protected Flux<BikeSiteDto> getMultipleFromSource(String sarea) {
        System.out.println("get from DB");
        return this.repository.findBySarea(sarea)
                .map(EntityDtoUtil::toDto);
    }
    @Override
    protected Mono<BikeSiteDto> updateCache(BikeSiteDto bikeSite) {
        System.out.println("update cache");
        return this.bikeGeo
                .add(bikeSite.getLongitude(), bikeSite.getLatitude(), bikeSite)
                .thenReturn(bikeSite);
    }

}
