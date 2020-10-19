package com.example.helpdesk;

public class Solutions {

    private String user_fix;
    private String problem;
    private String work;
    private String content;
    private String time_fix;

    public Solutions(){}

    public Solutions(String user_fix, String problem, String work, String content, String time_fix) {
        this.user_fix = user_fix;
        this.problem = problem;
        this.work = work;
        this.content = content;
        this.time_fix = time_fix;
    }

    public String getUser_fix() {
        return user_fix;
    }

    public void setUser_fix(String user_fix) {
        this.user_fix = user_fix;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime_fix() {
        return time_fix;
    }

    public void setTime_fix(String time_fix) {
        this.time_fix = time_fix;
    }


    }



