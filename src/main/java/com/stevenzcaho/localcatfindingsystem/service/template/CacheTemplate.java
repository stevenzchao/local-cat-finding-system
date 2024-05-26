package com.stevenzcaho.localcatfindingsystem.service.template;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public abstract class CacheTemplate<KEY, ENTITY> {

    public Mono<ENTITY> getSingle(int id) {
        return this.getSingleFromCache(id)
                .switchIfEmpty(this.getSingleFromSource(id)
                        .flatMap(this::updateCache));
    }

    public Flux<ENTITY> getMultiple(String sarea){
        return this.getMultipleFromCache(sarea)
                .switchIfEmpty(this.getMultipleFromSource(sarea)
                        .flatMap(this::updateCache));
    }

    abstract protected Mono<ENTITY> getSingleFromCache(Integer id);

    abstract protected Mono<ENTITY> getSingleFromSource(Integer id);

    abstract protected Flux<ENTITY> getMultipleFromCache(String sarea);
    abstract protected Flux<ENTITY> getMultipleFromSource(String sarea);
    abstract protected Mono<ENTITY> updateCache(ENTITY entityFlux);


}
