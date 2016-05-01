package com.remotehcs.remotehcs.record;


public class HistoryData {

    private String date;
    private String tobacco;
    private String relatives_diabetes;
    private String relatives_high_blood_pressure;

    public HistoryData() {
        this.date = "";
        this.tobacco = "";
        this.relatives_diabetes = "";
        this.relatives_high_blood_pressure = "";
    }

    public String getRelatives_high_blood_pressure() {
        return relatives_high_blood_pressure;
    }

    public void setRelatives_high_blood_pressure(String relatives_high_blood_pressure) {
        this.relatives_high_blood_pressure = relatives_high_blood_pressure;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTobacco() {
        return tobacco;
    }

    public void setTobacco(String tobacco) {
        this.tobacco = tobacco;
    }

    public String getRelatives_diabetes() {
        return relatives_diabetes;
    }

    public void setRelatives_diabetes(String relatives_diabetes) {
        this.relatives_diabetes = relatives_diabetes;
    }
}

