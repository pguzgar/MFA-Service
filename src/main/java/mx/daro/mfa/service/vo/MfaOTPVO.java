package mx.daro.mfa.service.vo;

import java.sql.Timestamp;

public class MfaOTPVO {
	
    private String channel;
    private String category;
    private String tokenId;
    private Long otpId;
    private String otpCode;
    private String status;
    private String type;
    private Timestamp createDt;
    private Timestamp updateDt;
    
    public String getChannel() {
        return this.channel;
    }
    
    public void setChannel(String channel) {
        this.channel = channel;
    }
    
    public String getCategory() {
        return this.category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getTokenId() {
        return this.tokenId;
    }
    
    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }
    
    public Long getOtpId() {
        return this.otpId;
    }
    
    public void setOtpId(Long otpId) {
        this.otpId = otpId;
    }
    
    public String getOtpCode() {
        return this.otpCode;
    }
    
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
    
    public String getStatus() {
        return this.status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Timestamp getCreateDt() {
        return this.createDt;
    }
    
    public void setCreateDt(Timestamp createDt) {
        this.createDt = createDt;
    }
    
    public Timestamp getUpdateDt() {
        return this.updateDt;
    }
    
    public void setUpdateDt(Timestamp updateDt) {
        this.updateDt = updateDt;
    }
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
}
