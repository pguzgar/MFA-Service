package mx.daro.mfa.service.utils;

import org.apache.commons.lang3.StringUtils;

import mx.daro.mfa.service.vo.MfaOTPVO;
import mx.daro.mfa.service.vo.MfaRequestVO;
import mx.daro.mfa.service.vo.MfaResponseVO;
import mx.daro.mfa.service.vo.MfaTokenVO;

public class MfaUtils {
	
    public static String generateRandomNumber() {
        String str = String.valueOf(Math.random());
        str = str.replaceAll("\\.", "");
        String randomNumber = str.substring(str.length() - 6, str.length());
        return randomNumber;
    }
    
    public static MfaTokenVO convert(MfaOTPVO otpVO) {
        MfaTokenVO tokenVO = new MfaTokenVO();
        tokenVO.setTokenId(otpVO.getTokenId());
        tokenVO.setChannel(otpVO.getChannel());
        tokenVO.setCategory(otpVO.getCategory());
        return tokenVO;
    }
    
    public static void validateTokenId(MfaRequestVO input) throws Exception {
        if (StringUtils.isEmpty(input.getTokenId())) {
            throw new Exception("Invalid Parameters");
        }
    }
    
    public static void validateActivationCode(MfaRequestVO input) throws Exception {
        if (StringUtils.isEmpty(input.getActivationCode())) {
            throw new Exception("Invalid Parameters");
        }
    }
    
    public static void validateOTPCode(MfaRequestVO input) throws Exception {
        if (StringUtils.isEmpty(input.getOtpCode())) {
            throw new Exception("Invalid Parameters");
        }
    }
    
    public static void setSystemError(MfaResponseVO vo, Throwable t) {
        vo.setResponseCd(Constants.SYSTEM_ERROR);
        vo.setDescription(Constants.SYSTEM_ERROR_DECRIPTION + " - " + t.getMessage());
    }
    
    public static void setSuccessResponse(MfaResponseVO vo) {
        vo.setResponseCd(Constants.SUCCESS);
        vo.setDescription(Constants.SUCCESS_DESCRIPTION);
    }
    
    public static void setInvalidActivationCodeResponse(MfaResponseVO vo) {
        vo.setResponseCd(Constants.INVALID_ACTIVATION_CODE);
        vo.setDescription(Constants.INVALID_ACTIVATION_CODE_DESCRIPTION);
    }
    
    public static void setInvalidOTPCodeResponse(MfaResponseVO vo) {
        vo.setResponseCd(Constants.INVALID_OTP_CODE);
        vo.setDescription(Constants.INVALID_OTP_CODE_DESCRIPTION);
    }
    
    public static void setTokenDoesntExists(MfaResponseVO vo) {
        vo.setResponseCd(Constants.TOKEN_DOES_NOT_EXISTS);
        vo.setDescription(Constants.TOKEN_DOES_NOT_EXISTS_DECRIPTION);
    }
    
    public static void setTokenExists(MfaResponseVO vo) {
        vo.setResponseCd(Constants.TOKEN_ALREADY_EXISTS);
        vo.setDescription(Constants.TOKEN_ALREADY_EXISTS_DECRIPTION);
    }
    
    public static void setTokenAlreadActivated(MfaResponseVO vo) {
        vo.setResponseCd(Constants.TOKEN_ALREADY_ACTIVATED);
        vo.setDescription(Constants.TOKEN_ALREADY_ACTIVATED_DESCRIPTION);
    }
    
    public static void setTokenNotActivated(MfaResponseVO vo) {
        vo.setResponseCd(Constants.TOKEN_NOT_ACTIVATED);
        vo.setDescription(Constants.TOKEN_NOT_ACTIVATED_DESCRIPTION);
    }
}
