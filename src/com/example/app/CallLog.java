package com.example.app;

public class CallLog {

    private int _id;
    private String caller_number;
    private String callee_status_id;

    public CallLog() {
    }

    public int getId() {
        return _id;
    }

    public void setId(int _id) {
        this._id = _id;
    }

    public String getCallerNumber() {
        return caller_number;
    }

    public void setCallerNumber(String caller_number) {
        this.caller_number = caller_number;
    }

    public String getCalleeStatusId() {
        return callee_status_id;
    }

    public void setCalleeStatusId(String callee_status_id) {
        this.callee_status_id = callee_status_id;
    }

}
