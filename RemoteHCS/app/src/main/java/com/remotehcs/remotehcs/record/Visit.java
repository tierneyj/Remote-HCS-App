package com.remotehcs.remotehcs.record;


import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class Visit {

    private int glucose;
    private int pulse;
    private int bps;
    private int bpd;
    private double height;
    private int weight;
    private int bmi;
    private String dry_mouth;
    private String high_blood_pressure;
    private String numbness;
    private String pregnant;
    private String dizziness;
    private String diabetes;
    private String date;
    private String user;

    public Visit() {
        this.glucose = 0;
        this.pulse = 0;
        this.bps = 0;
        this.bpd = 0;
        this.height = 0.0;
        this.weight = 0;
        this.bmi = 0;
        this.dry_mouth = "";
        this.high_blood_pressure = "";
        this.numbness = "";
        this.pregnant = "";
        this.dizziness = "";
        this.diabetes = "";
        this.date = timestamp();
        this.user = "";
    }

    public int getGlucose() {
        return glucose;
    }

    public void setGlucose(int glucose) {
        this.glucose = glucose;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getBps() {
        return bps;
    }

    public void setBps(int bps) {
        this.bps = bps;
    }

    public int getBpd() {
        return bpd;
    }

    public void setBpd(int bpd) {
        this.bpd = bpd;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getBmi() {
        return bmi;
    }

    public void setBmi(int bmi) {
        this.bmi = bmi;
    }

    public String getDry_mouth() {
        return dry_mouth;
    }

    public void setDry_mouth(String dry_mouth) {
        this.dry_mouth = dry_mouth;
    }

    public String getHigh_blood_pressure() {
        return high_blood_pressure;
    }

    public void setHigh_blood_pressure(String high_blood_pressure) {
        this.high_blood_pressure = high_blood_pressure;
    }

    public String getNumbness() {
        return numbness;
    }

    public void setNumbness(String numbness) {
        this.numbness = numbness;
    }

    public String getDiabetes() {
        return diabetes;
    }

    public void setDiabetes(String diabetes) {
        this.diabetes = diabetes;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDizziness() {
        return dizziness;
    }

    public void setDizziness(String dizziness) {
        this.dizziness = dizziness;
    }

    public String getPregnant() {
        return pregnant;
    }

    public void setPregnant(String pregnant) {
        this.pregnant = pregnant;
    }

    public String timestamp() {
        TimeZone timeZone = TimeZone.getTimeZone("UTC");
        Calendar timestamp = Calendar.getInstance(timeZone);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        simpleDateFormat.setTimeZone(timeZone);
        return simpleDateFormat.format(timestamp.getTime());
    }
}

