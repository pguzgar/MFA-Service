package mx.daro.mfa.service.vo;

public class MfaResponseVO {
	
    private String responseCd;
    private String description;
    private String activationOTPCode;
    
    public String getResponseCd() {
        return this.responseCd;
    }
    
    public void setResponseCd(String responseCd) {
        this.responseCd = responseCd;
    }
    
    public String getDescription() {
        return this.description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getActivationOTPCode() {
        return this.activationOTPCode;
    }
    
    public void setActivationOTPCode(String activationOTPCode) {
        this.activationOTPCode = activationOTPCode;
    }
    
}
