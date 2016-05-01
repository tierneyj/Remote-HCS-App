package com.remotehcs.remotehcs.record;

public class PostRequest {

    private Visit visit;
    private PatientData patient_data;
    private HistoryData history_data;
    private Metadata metadata;

    public PostRequest() {
        this.visit = new Visit();
        this.patient_data = new PatientData();
        this.history_data = new HistoryData();
        this.metadata = new Metadata();
    }

    public Visit getVisit() {
        return visit;
    }

    public void setVisit(Visit visit) {
        this.visit = visit;
    }

    public PatientData getPatient_data() {
        return patient_data;
    }

    public void setPatient_data(PatientData patient_data) {
        this.patient_data = patient_data;
    }

    public HistoryData getHistory_data() {
        return history_data;
    }

    public void setHistory_data(HistoryData history_data) {
        this.history_data = history_data;
    }

    public Metadata getMetadata() {
        return metadata;
    }

    public void setMetadata(Metadata metadata) {
        this.metadata = metadata;
    }
}
