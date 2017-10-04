package mx.daro.mfa.service.vo;

public class MfaRequestVO {
	
    private String channel;
    private String category;
    private String tokenId;
    private String otpCode;
    private String activationCode;
    
    public MfaRequestVO(String channel, String category, String tokenId, String otpCode, String activationCode) {
        this.channel = channel;
        this.category = category;
        this.tokenId = tokenId;
        this.otpCode = otpCode;
        this.activationCode = activationCode;
    }
    
    public String getOtpCode() {
        return this.otpCode;
    }
    
    public void setOtpCode(String otpCode) {
        this.otpCode = otpCode;
    }
    
    public String getActivationCode() {
        return this.activationCode;
    }
    
    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }
    
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
    
}
