package com.cs.qrcode.controls;

/**
 * Created by kstanoev on 1/14/2015.
 */
public class Job {

    private  String name, c_id, date,department,status;
    public Job(String name,String c_id,String date,String department,String status)
    {
        this.setName(name);
        this.setC_id(c_id);
        this.setDate(date);
        this.setDepartment(department);
        this.setStatus(status);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getC_id() {
        return c_id;
    }

    public void setC_id(String c_id) {
        this.c_id = c_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
