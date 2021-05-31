package com.bluebitsin.qrscanner.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class User implements Serializable {

    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("vechile_number")
    @Expose
    private String vechileNumber;
    @SerializedName("vechile_model")
    @Expose
    private String vechileModel;
    @SerializedName("mobile_number")
    @Expose
    private String mobileNumber;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("agent_code")
    @Expose
    private String agentCode;
    private final static long serialVersionUID = 6450953468846331069L;

    /**
     * No args constructor for use in serialization
     */
    public User() {
    }

    /**
     * @param vechileNumber
     * @param password
     * @param mobileNumber
     * @param userName
     * @param vechileModel
     * @param userId
     */
    public User(Integer userId, String agentCode, String userName, String vechileNumber, String vechileModel, String mobileNumber, String password) {
        super();
        this.userId = userId;
        this.userName = userName;
        this.vechileNumber = vechileNumber;
        this.vechileModel = vechileModel;
        this.mobileNumber = mobileNumber;
        this.password = password;
        this.agentCode = agentCode;
    }

    public String getAgentCode() { return agentCode; }

    public void setAgentCode(String agentCode) { this.agentCode = agentCode; }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVechileNumber() {
        return vechileNumber;
    }

    public void setVechileNumber(String vechileNumber) {
        this.vechileNumber = vechileNumber;
    }

    public String getVechileModel() {
        return vechileModel;
    }

    public void setVechileModel(String vechileModel) {
        this.vechileModel = vechileModel;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(User.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("userId");
        sb.append('=');
        sb.append(((this.userId == null) ? "<null>" : this.userId));
        sb.append(',');
        sb.append("userName");
        sb.append('=');
        sb.append(((this.userName == null) ? "<null>" : this.userName));
        sb.append(',');
        sb.append("agentCode");
        sb.append('=');
        sb.append(((this.agentCode == null) ? "<null>" : this.agentCode));
        sb.append(',');
        sb.append("vechileNumber");
        sb.append('=');
        sb.append(((this.vechileNumber == null) ? "<null>" : this.vechileNumber));
        sb.append(',');
        sb.append("vechileModel");
        sb.append('=');
        sb.append(((this.vechileModel == null) ? "<null>" : this.vechileModel));
        sb.append(',');
        sb.append("mobileNumber");
        sb.append('=');
        sb.append(((this.mobileNumber == null) ? "<null>" : this.mobileNumber));
        sb.append(',');
        sb.append("password");
        sb.append('=');
        sb.append(((this.password == null) ? "<null>" : this.password));
        sb.append(',');
        if (sb.charAt((sb.length() - 1)) == ',') {
            sb.setCharAt((sb.length() - 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }
}
