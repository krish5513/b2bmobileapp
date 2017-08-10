package com.rightclickit.b2bsaleon.beanclass;

import java.io.Serializable;

/**
 * Created by Sekhar Kuppa
 */

public class DeliverysBean implements Serializable {
    private String productId;
    private String productCode;
    private String productTitle;
    private String productAgentPrice;
    private String productConsumerPrice;
    private String productRetailerPrice;
    private String productgst;
    private String productvat;
    private double productOrderedQuantity;
    private double productStock;
    private double productExtraQuantity;
    private double productAvailableStockForSpecificAgent;
    private double selectedQuantity;
    private double taxAmount;
    private double productAmount;
    private double productRatePerUnit;
    private double productTaxPerUnit;
    private double deliveredQuantity;

    public String getProductgst() {
        return productgst;
    }

    public void setProductgst(String productgst) {
        this.productgst = productgst;
    }

    public String getProductvat() {
        return productvat;
    }

    public void setProductvat(String productvat) {
        this.productvat = productvat;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductAgentPrice() {
        return productAgentPrice;
    }

    public void setProductAgentPrice(String productAgentPrice) {
        this.productAgentPrice = productAgentPrice;
    }

    public String getProductConsumerPrice() {
        return productConsumerPrice;
    }

    public void setProductConsumerPrice(String productConsumerPrice) {
        this.productConsumerPrice = productConsumerPrice;
    }

    public String getProductRetailerPrice() {
        return productRetailerPrice;
    }

    public void setProductRetailerPrice(String productRetailerPrice) {
        this.productRetailerPrice = productRetailerPrice;
    }

    public double getProductOrderedQuantity() {
        return productOrderedQuantity;
    }

    public void setProductOrderedQuantity(double productOrderedQuantity) {
        this.productOrderedQuantity = productOrderedQuantity;
    }

    public double getProductStock() {
        return productStock;
    }

    public void setProductStock(double productStock) {
        this.productStock = productStock;
    }

    public double getProductExtraQuantity() {
        return productExtraQuantity;
    }

    public void setProductExtraQuantity(double productExtraQuantity) {
        this.productExtraQuantity = productExtraQuantity;
    }

    public double getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(double selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public double getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(double productAmount) {
        this.productAmount = productAmount;
    }

    public double getProductAvailableStockForSpecificAgent() {
        return productAvailableStockForSpecificAgent;
    }

    public void setProductAvailableStockForSpecificAgent(double productAvailableStockForSpecificAgent) {
        this.productAvailableStockForSpecificAgent = productAvailableStockForSpecificAgent;
    }

    public double getProductRatePerUnit() {
        return productRatePerUnit;
    }

    public void setProductRatePerUnit(double productRatePerUnit) {
        this.productRatePerUnit = productRatePerUnit;
    }

    public double getProductTaxPerUnit() {
        return productTaxPerUnit;
    }

    public void setProductTaxPerUnit(double productTaxPerUnit) {
        this.productTaxPerUnit = productTaxPerUnit;
    }

    public double getDeliveredQuantity() {
        return deliveredQuantity;
    }

    public void setDeliveredQuantity(double deliveredQuantity) {
        this.deliveredQuantity = deliveredQuantity;
    }
}
