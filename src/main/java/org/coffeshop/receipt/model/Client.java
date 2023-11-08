package org.coffeshop.receipt.model;

public class Client {

    private Long stampCount;

    public Client(Long stampCount) {
        this.stampCount = stampCount;
    }

    public Long getStampCount() {
        return stampCount;
    }

    public void setStampCount(Long stampCount) {
        this.stampCount = stampCount;
    }
}
