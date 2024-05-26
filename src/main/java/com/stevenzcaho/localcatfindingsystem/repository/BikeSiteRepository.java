package com.stevenzcaho.localcatfindingsystem.repository;

import com.stevenzcaho.localcatfindingsystem.entity.BikeSite;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface BikeSiteRepository extends ReactiveCrudRepository<BikeSite,Integer> {

    Flux<BikeSite> findBySarea(String sarea);
}
