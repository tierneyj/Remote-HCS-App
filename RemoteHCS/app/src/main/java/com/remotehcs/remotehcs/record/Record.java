package com.remotehcs.remotehcs.record;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
public class Record {

    private HistoryData historyData;
    private PatientData patientData;
    private Metadata metadata;
    public ArrayList<Visit> visits;

    public Record() {
        this.historyData = new HistoryData();
        this.patientData = new PatientData();
        this.metadata = new Metadata();
        this.visits = new ArrayList<Visit>();
    }

    public HistoryData getHistoryData() {
        return historyData;
    }

    public void setHistoryData(HistoryData historyData) {
        this.historyData = historyData;
    }

    public PatientData getPatientData() {
        return patientData;
    }

    public void setPatientData(PatientData patientData) {
        this.patientData = patientData;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }

    public ArrayList<Visit> getVisits() {
        return visits;
    }

    public void setVisits(ArrayList<Visit> visits) {
        this.visits = visits;
    }

    public Visit getVisit(int i) {
        return visits.get(i);
    }
}
