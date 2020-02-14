package com.apkglobal.memoapp.Model;

public class Memo {

    private String task;
    private String dateAdded;
    private int id;

    public Memo() {

    }

    public Memo(String task, String dateAdded, int id) {
        this.task = task;
        this.dateAdded = dateAdded;
        this.id = id;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
