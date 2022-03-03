package it.unibo.disi.harvesteradapter.entity.json_model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HarvesterOutput {
    
    private String devId;
    private String harvId;

    private Integer batState;
    private Float batLifeh;
    private Float tChargeh;
    private Float dSOCrate;
    private Integer simStatus;

    private String date;

    public HarvesterOutput(){}

    public HarvesterOutput(String devId, String harvId, Integer batState, Float batLifeh, Float dSOCrate, Float tChargeh, Integer simStatus, String date) {
        this.devId = devId;
        this.batState = batState;
        this.harvId = harvId;
        this.batLifeh = batLifeh;
        this.dSOCrate = dSOCrate;
        this.tChargeh = tChargeh;
        this.simStatus = simStatus;
        this.date = date;
    }


    @JsonProperty("devId")
    public String getDevId() {
        return this.devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    @JsonProperty("harvId")
    public String getHarvId() {
        return this.harvId;
    }

    public void setHarvId(String harvId) {
        this.harvId = harvId;
    }

    @JsonProperty("batState")
    public Integer getBatState() {
        return this.batState;
    }
    
    public void setBatState(Integer batState) {
        this.batState = batState;
    }

    @JsonProperty("batlifeh")
    public Float getBatLifeh() {
        return this.batLifeh;
    }

    public void setBatLifeh(Float batLifeh) {
        this.batLifeh = batLifeh;
    }

    @JsonProperty("tChargeh")
    public Float getTChargeh() {
        return this.tChargeh;
    }

    public void setTChargeh(Float tChargeh) {
        this.tChargeh = tChargeh;
    }

    @JsonProperty("dSOCrate")
    public Float getDSOCrate() {
        return this.dSOCrate;
    }

    public void setDSOCrate(Float dSOCrate) {
        this.dSOCrate = dSOCrate;
    }

    @JsonProperty("simStatus")
    public Integer getSimStatus() {
        return this.simStatus;
    }

    public void setSimStatus(Integer simStatus) {
        this.simStatus = simStatus;
    }

    @JsonProperty("date")
    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    
}
