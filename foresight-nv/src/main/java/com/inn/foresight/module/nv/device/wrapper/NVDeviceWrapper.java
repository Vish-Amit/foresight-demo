package com.inn.foresight.module.nv.device.wrapper;

import java.util.List;

import com.inn.core.generic.wrapper.RestWrapper;

@RestWrapper
public class NVDeviceWrapper {

    private String deviceId;
    private List<String> operator;
    private List<String>  os;
    private List<String> make;
    private List<String> model;
    private List<String> source;
    private List<String> appVersion;

    public String getDeviceId() { return deviceId; }

    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }

    public List<String> getOperator() {
        return operator;
    }

    public void setOperator(List<String> operator) {
        this.operator = operator;
    }

    public List<String> getOs() {
        return os;
    }

    public void setOs(List<String> os) {
        this.os = os;
    }

    public List<String> getMake() {
        return make;
    }

    public void setMake(List<String> make) {
        this.make = make;
    }

    public List<String> getModel() {
        return model;
    }

    public void setModel(List<String> model) {
        this.model = model;
    }

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public List<String> getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(List<String> appVersion) {
        this.appVersion = appVersion;
    }

    @Override
    public String toString() {
        return "NVDeviceWrapper{" +
                "deviceId='" + deviceId + '\'' +
                ", operator=" + operator +
                ", os=" + os +
                ", make=" + make +
                ", model=" + model +
                ", source=" + source +
                ", appVersion=" + appVersion +
                '}';
    }
}
