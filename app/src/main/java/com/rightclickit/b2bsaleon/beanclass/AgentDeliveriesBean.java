package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 8/17/2017.
 */

public class AgentDeliveriesBean {
    public String getTripNo() {
        return tripNo;
    }

    public void setTripNo(String tripNo) {
        this.tripNo = tripNo;
    }

    String tripNo;
    String tripDate;



    public String getTripDate() {
        return tripDate;
    }

    public void setTripDate(String tripDate) {
        this.tripDate = tripDate;
    }

    public String getDeliverdstatus() {
        return Deliverdstatus;
    }

    public void setDeliverdstatus(String deliverdstatus) {
        Deliverdstatus = deliverdstatus;
    }

    public String getDeliveredItems() {
        return DeliveredItems;
    }

    public void setDeliveredItems(String deliveredItems) {
        DeliveredItems = deliveredItems;
    }

    public String getDeliveredBy() {
        return deliveredBy;
    }

    public void setDeliveredBy(String deliveredBy) {
        this.deliveredBy = deliveredBy;
    }

    String Deliverdstatus;
    String DeliveredItems;
    String deliveredBy;

}
