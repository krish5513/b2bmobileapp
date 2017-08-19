package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 8/19/2017.
 */

public class AgentReturnsBean {
    String returnNo;
    String returnDate;
    String returnStatus;

    public String getReturnNo() {
        return returnNo;
    }

    public void setReturnNo(String returnNo) {
        this.returnNo = returnNo;
    }

    public String getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(String returnDate) {
        this.returnDate = returnDate;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getReturnedItems() {
        return ReturnedItems;
    }

    public void setReturnedItems(String returnedItems) {
        ReturnedItems = returnedItems;
    }

    public String getReturnedBy() {
        return ReturnedBy;
    }

    public void setReturnedBy(String returnedBy) {
        ReturnedBy = returnedBy;
    }

    String ReturnedItems;
    String ReturnedBy;

}
