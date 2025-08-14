package com.healthtracker.model;

public class ConsultationStats {
    private int pending;
    private int active;
    private int completed;
    private int total;

    public ConsultationStats(int pending, int active, int completed, int total) {
        this.pending = pending;
        this.active = active;
        this.completed = completed;
        this.total = total;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
