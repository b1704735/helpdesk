package com.example.helpdesk;

//Chức năng: Class để tạo từng đối tượng item trong listview trong KTV Activity

public class Works {
    private String work_name;
    private String deadline;
    private int status;

    public Works(String work_name, String deadline, int status) {
        this.work_name = work_name;
        this.deadline = deadline;
        this.status = status;
    }

    public String getWork_name() {
        return work_name;
    }

    public void setWork_name(String work_name) {
        this.work_name = work_name;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
