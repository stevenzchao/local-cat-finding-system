package com.stevenzcaho.localcatfindingsystem.util;

import com.stevenzcaho.localcatfindingsystem.dto.BikeSiteDto;
import com.stevenzcaho.localcatfindingsystem.entity.BikeSite;
import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static BikeSiteDto toDto(BikeSite bikeSite){
        BikeSiteDto dto = new BikeSiteDto();
        BeanUtils.copyProperties(bikeSite, dto);
        return dto;
    }

    public static BikeSite toEntity(BikeSiteDto dto){
        BikeSite bikeSite = new BikeSite();
        BeanUtils.copyProperties(dto, bikeSite);
        return bikeSite;
    }

}
