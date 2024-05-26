package com.stevenzcaho.localcatfindingsystem.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BikeSite {

    @Id
    private Integer id;
    private String sno;
    private String sna;
    private String sarea;
    private String mday;
    private String ar;
    private String sareaen;
    private String snaen;
    private String aren;
    private String act;
    private String srcUpdateTime;
    private String updateTime;
    private String infoTime;
    private String infoDate;
    private Integer total;
    @JsonProperty("available_rent_bikes")
    private Integer availableRentBikes;
    private double latitude;
    private double longitude;
    @JsonProperty("available_return_bikes")
    private Integer availableReturnBikes;

}