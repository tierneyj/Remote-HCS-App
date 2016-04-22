package com.remotehcs.remotehcs.record;


public class VisitResponse
{
    private Visit[] visits;

    private HistoryData history_data;

    public Visit[] getVisits() {
        return visits;
    }

    public Visit getVisits(int i) {
        return visits[i];
    }

    public void setVisits(Visit[] visits) {
        this.visits = visits;
    }

    public HistoryData getHistory_data() {
        return history_data;
    }

    public void setHistory_data(HistoryData history_data) {
        this.history_data = history_data;
    }
}
