package com.jone.record.kbase.entity;

public class JournalCatalog {

    private String title;
    private String parentGuid;
    private String currentGuid;
    private String orderId;

    public JournalCatalog() {
        super();
    }

    public JournalCatalog(String title, String parentGuid, String currentGuid, String orderId) {
        this.title = title;
        this.parentGuid = parentGuid;
        this.currentGuid = currentGuid;
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getParentGuid() {
        return parentGuid;
    }

    public void setParentGuid(String parentGuid) {
        this.parentGuid = parentGuid;
    }

    public String getCurrentGuid() {
        return currentGuid;
    }

    public void setCurrentGuid(String currentGuid) {
        this.currentGuid = currentGuid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}
