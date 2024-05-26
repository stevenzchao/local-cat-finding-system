package com.stevenzcaho.localcatfindingsystem.controller;

import com.stevenzcaho.localcatfindingsystem.dto.BikeSiteDto;
import com.stevenzcaho.localcatfindingsystem.service.BikeSiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("map")
public class MapController {

    @Autowired
    private BikeSiteService service;

    @GetMapping("single/{id}")
    public Mono<BikeSiteDto> getSingleGatheringSite( @PathVariable Integer id){
        return this.service.getSingleBikeSite(id);
    }
    @GetMapping("all")
    public Flux<BikeSiteDto> getAllGatheringSites(){
        return this.service.getAllBikeSites();
    }


    @GetMapping("{district}")
    public Flux<BikeSiteDto> getBikeSitesInDistrict(@PathVariable String district) {
        System.out.println("district : " + district);
        return this.service.getBikeSitesInDistrict(district);
    }


}
