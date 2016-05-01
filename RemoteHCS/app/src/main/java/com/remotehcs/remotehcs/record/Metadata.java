package com.remotehcs.remotehcs.record;

/**
 * Created by jeltierney on 4/30/16.
 */
public class Metadata {

    private String name;
    private String patient_exists;
    private Double lat;
    private Double lon;
    private int internet;
    private String duration;
    private String date;
    private String pubpid;

    public Metadata() {
        this.name = "";
        this.patient_exists = "";
        this.lat = 0.0;
        this.lon = 0.0;
        this.internet = 0;
        this.duration = "";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPatient_exists() {
        return patient_exists;
    }

    public void setPatient_exists(String patient_exists) {
        this.patient_exists = patient_exists;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public int getInternet() {
        return internet;
    }

    public void setInternet(int internet) {
        this.internet = internet;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPubpid() {
        return pubpid;
    }

    public void setPubpid(String pubpid) {
        this.pubpid = pubpid;
    }
}
