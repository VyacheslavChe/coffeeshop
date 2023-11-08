package org.coffeshop.receipt.model;

public class Receipt {
    String receiptText;
    Long earnedStamps;

    public String getReceiptText() {
        return receiptText;
    }

    public void setReceiptText(String receiptText) {
        this.receiptText = receiptText;
    }

    public Long getEarnedStamps() {
        return earnedStamps;
    }

    public void setEarnedStamps(Long earnedStamps) {
        this.earnedStamps = earnedStamps;
    }
}
