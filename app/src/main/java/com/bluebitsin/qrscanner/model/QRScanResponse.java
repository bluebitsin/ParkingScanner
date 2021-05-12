
package com.bluebitsin.qrscanner.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QRScanResponse implements Serializable
{

    @SerializedName("response_status")
    @Expose
    private Boolean responseStatus;
    @SerializedName("response_code")
    @Expose
    private Integer responseCode;
    @SerializedName("qr_data")
    @Expose
    private QrData qrData;
    private final static long serialVersionUID = 8766229313368434133L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public QRScanResponse() {
    }

    /**
     * 
     * @param qrData
     * @param responseStatus
     * @param responseCode
     */
    public QRScanResponse(Boolean responseStatus, Integer responseCode, QrData qrData) {
        super();
        this.responseStatus = responseStatus;
        this.responseCode = responseCode;
        this.qrData = qrData;
    }

    public Boolean getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(Boolean responseStatus) {
        this.responseStatus = responseStatus;
    }

    public Integer getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public QrData getQrData() {
        return qrData;
    }

    public void setQrData(QrData qrData) {
        this.qrData = qrData;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(QRScanResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("responseStatus");
        sb.append('=');
        sb.append(((this.responseStatus == null)?"<null>":this.responseStatus));
        sb.append(',');
        sb.append("responseCode");
        sb.append('=');
        sb.append(((this.responseCode == null)?"<null>":this.responseCode));
        sb.append(',');
        sb.append("qrData");
        sb.append('=');
        sb.append(((this.qrData == null)?"<null>":this.qrData));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
