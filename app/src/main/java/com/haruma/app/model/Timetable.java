package com.haruma.app.model;

public class Timetable {
    private int timeTableId;
    private String name;
    private String day;
    private String note;
    private String startTime;
    private String endTime;
    private int userId;
    private boolean status;

    public Timetable() {
    }

    public Timetable(int timeTableId, String name, String day, String note, String startTime, String endTime, int userId, int status) {
        this.timeTableId = timeTableId;
        this.name = name;
        this.day = day;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
        this.status = status != 0;
    }

    public int getTimeTableId() {
        return timeTableId;
    }

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public String getNote() {
        return note;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getUserId() {
        return userId;
    }

    public void setTimeTableId(int timeTableId) {
        this.timeTableId = timeTableId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}

