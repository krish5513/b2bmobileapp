package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 8/20/2017.
 */

public class AgentPaymentsBean {
    String payment_Number;
    String payment_date;
    String payment_status;

    public String getPayment_Number() {
        return payment_Number;
    }

    public void setPayment_Number(String payment_Number) {
        this.payment_Number = payment_Number;
    }

    public String getPayment_date() {
        return payment_date;
    }

    public void setPayment_date(String payment_date) {
        this.payment_date = payment_date;
    }

    public String getPayment_status() {
        return payment_status;
    }

    public void setPayment_status(String payment_status) {
        this.payment_status = payment_status;
    }

    public String getPayment_OBamount() {
        return payment_OBamount;
    }

    public void setPayment_OBamount(String payment_OBamount) {
        this.payment_OBamount = payment_OBamount;
    }

    public String getPayment_ordervalue() {
        return payment_ordervalue;
    }

    public void setPayment_ordervalue(String payment_ordervalue) {
        this.payment_ordervalue = payment_ordervalue;
    }

    public String getPayment_totalamount() {
        return payment_totalamount;
    }

    public void setPayment_totalamount(String payment_totalamount) {
        this.payment_totalamount = payment_totalamount;
    }

    public String getPayment_totaldue() {
        return payment_totaldue;
    }

    public void setPayment_totaldue(String payment_totaldue) {
        this.payment_totaldue = payment_totaldue;
    }

    String payment_OBamount;
    String payment_ordervalue;
    String payment_totalamount;
    String payment_totaldue;
}
