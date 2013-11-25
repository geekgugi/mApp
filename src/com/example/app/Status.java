package com.example.app;

public class Status {

    private int _id;
    private String status_message;

    public Status() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getStatusMessage() {
        return status_message;
    }

    public void setStatusMessage(String status_message) {
        this.status_message = status_message;
    }
}
