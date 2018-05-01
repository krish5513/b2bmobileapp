package com.rightclickit.b2bsaleon.beanclass;

public class Nextdayindent_moreinfoBeen {

    private String id;
    private String date;
    private String milk;
    private String curd;
    private String other;

    public Nextdayindent_moreinfoBeen(String date, String milk, String curd, String other) {
        this.date = date;
        this.milk = milk;
        this.curd = curd;
        this.other = other;

    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMilk() {
        return milk;
    }

    public void setMilk(String milk) {
        this.milk = milk;
    }

    public String getCurd() {
        return curd;
    }

    public void setCurd(String curd) {
        this.curd = curd;
    }

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

}
