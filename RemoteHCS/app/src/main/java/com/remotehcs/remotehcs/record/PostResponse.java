package com.remotehcs.remotehcs.record;


public class PostResponse {

    private String message;

    private PatientData data;

    public PatientData getData() {
        return data;
    }

    public void setData(PatientData data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
