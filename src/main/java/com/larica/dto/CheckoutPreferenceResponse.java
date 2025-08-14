package com.larica.dto;

public class CheckoutPreferenceResponse {

    private String initPoint;
    private String sandboxInitPoint;
    private String preferenceId;

    public CheckoutPreferenceResponse() {
    }

    public CheckoutPreferenceResponse(String initPoint, String sandboxInitPoint, String preferenceId) {
        this.initPoint = initPoint;
        this.sandboxInitPoint = sandboxInitPoint;
        this.preferenceId = preferenceId;
    }

    public String getInitPoint() {
        return initPoint;
    }

    public void setInitPoint(String initPoint) {
        this.initPoint = initPoint;
    }

    public String getSandboxInitPoint() {
        return sandboxInitPoint;
    }

    public void setSandboxInitPoint(String sandboxInitPoint) {
        this.sandboxInitPoint = sandboxInitPoint;
    }

    public String getPreferenceId() {
        return preferenceId;
    }

    public void setPreferenceId(String preferenceId) {
        this.preferenceId = preferenceId;
    }

    @Override
    public String toString() {
        return "CheckoutPreferenceResponse{" +
                "initPoint='" + initPoint + '\'' +
                ", sandboxInitPoint='" + sandboxInitPoint + '\'' +
                ", preferenceId='" + preferenceId + '\'' +
                '}';
    }
}
