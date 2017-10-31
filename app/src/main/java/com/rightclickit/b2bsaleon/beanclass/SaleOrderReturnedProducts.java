package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by venkat on 8/11/17.
 */

public class SaleOrderReturnedProducts implements Serializable {
    private String id;
    private String name;
    private String code;
    private String openingBalance;
    private String delivered;
    private String returned;
    private String closingBalance;
    private String returnno;


    public String getReturnno() {
        return returnno;
    }

    public void setReturnno(String returnno) {
        this.returnno = returnno;
    }

    public String getReturndate() {
        return returndate;
    }

    public void setReturndate(String returndate) {
        this.returndate = returndate;
    }

    private String returndate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOpeningBalance() {
        return openingBalance;
    }

    public void setOpeningBalance(String openingBalance) {
        this.openingBalance = openingBalance;
    }

    public String getDelivered() {
        return delivered;
    }

    public void setDelivered(String delivered) {
        this.delivered = delivered;
    }

    public String getReturned() {
        return returned;
    }

    public void setReturned(String returned) {
        this.returned = returned;
    }

    public String getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(String closingBalance) {
        this.closingBalance = closingBalance;
    }

    @Override
    public String toString() {
        return "SaleOrderReturnedProducts{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", openingBalance='" + openingBalance + '\'' +
                ", delivered='" + delivered + '\'' +
                ", returned='" + returned + '\'' +
                ", closingBalance='" + closingBalance + '\'' +
                ", rno='" + returnno + '\'' +
                ", rdate='" + returndate + '\'' +
                '}';
    }
}
