package com.cs.qrcode.controls;

/**
 * Created by kstanoev on 1/14/2015.
 */
public class Unjob {

    private  String name, c_id, unit,department;
    public Unjob(String name, String c_id, String unit, String department)
    {
        this.setName(name);
        this.setC_id(c_id);
        this.setUnit(unit);
        this.setDepartment(department);
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

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
