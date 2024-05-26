package com.stevenzcaho.localcatfindingsystem.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stevenzcaho.localcatfindingsystem.dto.BikeSiteDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class BikeSiteUtil {

    public static List<BikeSiteDto> getGatheringSites(){
        ObjectMapper mapper = new ObjectMapper();
        InputStream stream = BikeSiteUtil.class.getClassLoader().getResourceAsStream("bikeSites.json");
        try{
            return mapper.readValue(stream, new TypeReference<List<BikeSiteDto>>() {
            });
        }catch (IOException e){
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
