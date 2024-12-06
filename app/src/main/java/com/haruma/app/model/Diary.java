package com.haruma.app.model;

public class Diary {
    private int diaryId;
    private String name;
    private String day;
    private String note;
    private String startTime;
    private String endTime;
    private int userId;

    public Diary() {
    }

    public Diary(int diaryId, String name, String day, String note, String startTime, String endTime, int userId) {
        this.diaryId = diaryId;
        this.name = name;
        this.day = day;
        this.note = note;
        this.startTime = startTime;
        this.endTime = endTime;
        this.userId = userId;
    }

    public int getDiaryId() {
        return diaryId;
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

    public void setDiaryId(int diaryId) {
        this.diaryId = diaryId;
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
}

