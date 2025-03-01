package com.system.model;

public class BillingSetting {
    private int settingId;
    private String settingName;
    private float settingValue;
    private String description;

    public BillingSetting() {
    }

    public BillingSetting(int settingId, String settingName, float settingValue, String description) {
        this.settingId = settingId;
        this.settingName = settingName;
        this.settingValue = settingValue;
        this.description = description;
    }

    // Getters and setters for all fields
    public int getSettingId() {
        return settingId;
    }

    public void setSettingId(int settingId) {
        this.settingId = settingId;
    }

    public String getSettingName() {
        return settingName;
    }

    public void setSettingName(String settingName) {
        this.settingName = settingName;
    }

    public float getSettingValue() {
        return settingValue;
    }

    public void setSettingValue(float settingValue) {
        this.settingValue = settingValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}