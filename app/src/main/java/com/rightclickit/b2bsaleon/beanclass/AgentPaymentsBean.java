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



    String payment_amount;

    public String getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(String payment_amount) {
        this.payment_amount = payment_amount;
    }

    public String getPayment_mop() {
        return payment_mop;
    }

    public void setPayment_mop(String payment_mop) {
        this.payment_mop = payment_mop;
    }

    public String getPayment_checkno() {
        return payment_checkno;
    }

    public void setPayment_checkno(String payment_checkno) {
        this.payment_checkno = payment_checkno;
    }

    public String getPayment_checkDate() {
        return payment_checkDate;
    }

    public void setPayment_checkDate(String payment_checkDate) {
        this.payment_checkDate = payment_checkDate;
    }

    public String getPayment_bankName() {
        return payment_bankName;
    }

    public void setPayment_bankName(String payment_bankName) {
        this.payment_bankName = payment_bankName;
    }

    String payment_mop;
    String payment_checkno;
    String payment_checkDate;
    String payment_bankName;
}
