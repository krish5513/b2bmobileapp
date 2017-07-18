package com.rightclickit.b2bsaleon.beanclass;

/**
 * Created by PPS on 7/17/2017.
 */

public class TripSheetPaymentsBean {
    private String paymentsAmount;

    public String getPaymentsAmount() {
        return paymentsAmount;
    }

    public void setPaymentsAmount(String paymentsAmount) {
        this.paymentsAmount = paymentsAmount;
    }

    public String getPaymentMOP() {
        return paymentMOP;
    }

    public void setPaymentMOP(String paymentMOP) {
        this.paymentMOP = paymentMOP;
    }

    private String paymentMOP;

}
