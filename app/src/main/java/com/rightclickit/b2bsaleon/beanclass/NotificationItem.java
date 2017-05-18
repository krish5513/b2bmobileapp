package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 5/18/2017.
 */

public class NotificationItem {
    public NotificationItem(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    String description;
    String date;
}
