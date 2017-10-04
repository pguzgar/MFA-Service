package mx.daro.mfa.service.utils;

public class Constants {
	
    public static String DATA_SOURCE_NAME = "jdbc/mfaDS";
    public static String REGISTERED = "R";
    public static String ACTIVATED = "A";
    public static String AUTHENTICATED = "A";
    
    public static String SUCCESS = "MFA0000";
    public static String SUCCESS_DESCRIPTION = "Success";
    
    public static String INVALID_ACTIVATION_CODE = "MFA0001";
    public static String INVALID_ACTIVATION_CODE_DESCRIPTION = "Invalid activation code";
    
    public static String INVALID_OTP_CODE = "MFA0002";
    public static String INVALID_OTP_CODE_DESCRIPTION = "Invalid OTP code";
    
    public static String SYSTEM_ERROR = "MFA0003";
    public static String SYSTEM_ERROR_DECRIPTION = "System Error";
    
    public static String TOKEN_DOES_NOT_EXISTS = "MFA0004";
    public static String TOKEN_DOES_NOT_EXISTS_DECRIPTION = "Token doesn't exists";
    
    public static String TOKEN_ALREADY_EXISTS = "MFA0005";
    public static String TOKEN_ALREADY_EXISTS_DECRIPTION = "Token already exists";
    
    public static String TOKEN_ALREADY_ACTIVATED = "MFA0006";
    public static String TOKEN_ALREADY_ACTIVATED_DESCRIPTION = "Token already activated";
    
    public static String TOKEN_NOT_ACTIVATED = "MFA0007";
    public static String TOKEN_NOT_ACTIVATED_DESCRIPTION = "Token not activated";
    
}
