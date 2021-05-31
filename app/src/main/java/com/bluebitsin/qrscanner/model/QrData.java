
package com.bluebitsin.qrscanner.model;

import java.io.Serializable;
import java.util.Date;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class QrData implements Serializable
{

    @SerializedName("is_qr_valid")
    @Expose
    private Boolean isQrValid;
    @SerializedName("scan_qr_status")
    @Expose
    private Integer scanQrStatus;
    @SerializedName("scan_status_message")
    @Expose
    private String scanStatusMessage;
    @SerializedName("booking_id")
    @Expose
    private Integer bookingId;
    @SerializedName("booking_timestamp")
    @Expose
    private Date bookingTimestamp;
    @SerializedName("car_model")
    @Expose
    private String carModel;
    @SerializedName("car_no")
    @Expose
    private String carNo;
    private final static long serialVersionUID = 7826930786530366003L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public QrData() {
    }

    /**
     * 
     * @param isQrValid
     * @param scanQrStatus
     * @param carNo
     * @param scanStatusMessage
     * @param bookingTimestamp
     * @param bookingId
     * @param carModel
     */
    public QrData(Boolean isQrValid, Integer scanQrStatus, String scanStatusMessage,
                  Integer bookingId, Date bookingTimestamp, String carModel, String carNo) {
        super();
        this.isQrValid = isQrValid;
        this.scanQrStatus = scanQrStatus;
        this.scanStatusMessage = scanStatusMessage;
        this.bookingId = bookingId;
        this.bookingTimestamp = bookingTimestamp;
        this.carModel = carModel;
        this.carNo = carNo;
    }

    public Boolean getIsQrValid() {
        return isQrValid;
    }

    public void setIsQrValid(Boolean isQrValid) {
        this.isQrValid = isQrValid;
    }

    public Integer getScanQrStatus() {
        return scanQrStatus;
    }

    public void setScanQrStatus(Integer scanQrStatus) {
        this.scanQrStatus = scanQrStatus;
    }

    public String getScanStatusMessage() {
        return scanStatusMessage;
    }

    public void setScanStatusMessage(String scanStatusMessage) {
        this.scanStatusMessage = scanStatusMessage;
    }

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Date getBookingTimestamp() {
        return bookingTimestamp;
    }

    public void setBookingTimestamp(Date bookingTimestamp) {
        this.bookingTimestamp = bookingTimestamp;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarNo() {
        return carNo;
    }

    public void setCarNo(String carNo) {
        this.carNo = carNo;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(QrData.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("isQrValid");
        sb.append('=');
        sb.append(((this.isQrValid == null)?"<null>":this.isQrValid));
        sb.append(',');
        sb.append("scanQrStatus");
        sb.append('=');
        sb.append(((this.scanQrStatus == null)?"<null>":this.scanQrStatus));
        sb.append(',');
        sb.append("scanStatusMessage");
        sb.append('=');
        sb.append(((this.scanStatusMessage == null)?"<null>":this.scanStatusMessage));
        sb.append(',');
        sb.append("bookingId");
        sb.append('=');
        sb.append(((this.bookingId == null)?"<null>":this.bookingId));
        sb.append(',');
        sb.append("bookingTimestamp");
        sb.append('=');
        sb.append(((this.bookingTimestamp == null)?"<null>":this.bookingTimestamp.toString()));
        sb.append(',');
        sb.append("carModel");
        sb.append('=');
        sb.append(((this.carModel == null)?"<null>":this.carModel));
        sb.append(',');
        sb.append("carNo");
        sb.append('=');
        sb.append(((this.carNo == null)?"<null>":this.carNo));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
