package mx.daro.mfa.service.business;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.bind.annotation.XmlElement;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import mx.daro.mfa.service.business.dao.MfaServiceDAO;
import mx.daro.mfa.service.utils.Constants;
import mx.daro.mfa.service.utils.MfaUtils;
import mx.daro.mfa.service.vo.MfaOTPVO;
import mx.daro.mfa.service.vo.MfaRequestVO;
import mx.daro.mfa.service.vo.MfaResponseVO;
import mx.daro.mfa.service.vo.MfaTokenVO;

@WebService(serviceName = "MfaService")
public class MfaService extends SpringBeanAutowiringSupport {
	
    @Autowired
    private MfaServiceDAO serviceDao;
    
    /**
     * 
     * This method is used to register a token and send an activation code.
     * Token will be marked as registered on MFA
     * 
     * @param channel
     * @param category
     * @param tokenId
     * @return
     */
    @WebMethod
    public MfaResponseVO registerToken(@WebParam(name = "channel") String channel, @WebParam(name = "category") String category, @XmlElement(required = true, nillable = false) @WebParam(name = "tokenId") String tokenId) {
        System.out.println("MFA registerToken");
        MfaResponseVO output = new MfaResponseVO();
        
        try {
            MfaRequestVO input = new MfaRequestVO(channel, category, tokenId, null, null);
            MfaTokenVO tokenVO = new MfaTokenVO();
            tokenVO.setChannel(input.getChannel());
            tokenVO.setCategory(input.getCategory());
            if (StringUtils.isEmpty(tokenVO.getChannel())) {
                tokenVO.setChannel("DEFAULT");
            }
            if (StringUtils.isEmpty(tokenVO.getCategory())) {
                tokenVO.setCategory("DEFAULT");
            }
            tokenVO.setTokenId(input.getTokenId());
            MfaUtils.validateTokenId(input);
            if (!serviceDao.tokenExists(tokenVO)) {
                tokenVO.setStatus(Constants.REGISTERED);
                String activationCode = MfaUtils.generateRandomNumber();
                serviceDao.insertToken(tokenVO);
                MfaOTPVO otp = new MfaOTPVO();
                otp.setCategory(tokenVO.getCategory());
                otp.setChannel(tokenVO.getChannel());
                otp.setTokenId(tokenVO.getTokenId());
                otp.setOtpCode(activationCode);
                otp.setType("R");
                serviceDao.insertOTP(otp);
                output.setActivationOTPCode(activationCode);
                MfaUtils.setSuccessResponse(output);
            } else {
                MfaUtils.setTokenExists(output);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            MfaUtils.setSystemError(output, e);
            return output;
        }
    }
    
    /**
     * 
     * This method is to activate the token.
	 * After token is registered and received the activation code, this method will get that code
	 * to activate the token and it will send the OTP to access the system. 
	 * 
     * @param channel
     * @param category
     * @param tokenId
     * @param activationCode
     * @return
     */
    @WebMethod
    public MfaResponseVO activateToken(@WebParam(name = "channel") String channel, @WebParam(name = "category") String category, @XmlElement(required = true, nillable = false) @WebParam(name = "tokenId") String tokenId, @XmlElement(required = true, nillable = false) @WebParam(name = "activationCode") String activationCode) {
        System.out.println("MFA activateToken");
        MfaResponseVO output = new MfaResponseVO();
        try {
            MfaRequestVO input = new MfaRequestVO(channel, category, tokenId, null, activationCode);
            MfaTokenVO tokenVO = new MfaTokenVO();
            tokenVO.setTokenId(input.getTokenId());
            tokenVO.setChannel(input.getChannel());
            tokenVO.setCategory(input.getCategory());
            if (StringUtils.isEmpty(tokenVO.getChannel())) {
                tokenVO.setChannel("DEFAULT");
            }
            if (StringUtils.isEmpty(tokenVO.getCategory())) {
                tokenVO.setCategory("DEFAULT");
            }
            MfaUtils.validateTokenId(input);
            MfaUtils.validateActivationCode(input);
            if (serviceDao.tokenExists(tokenVO)) {
                if (!serviceDao.tokenActivated(tokenVO)) {
                    if (validateActivationCode(input, tokenVO)) {
                        tokenVO.setStatus(Constants.ACTIVATED);
                        MfaOTPVO otp = new MfaOTPVO();
                        otp.setCategory(tokenVO.getCategory());
                        otp.setChannel(tokenVO.getChannel());
                        otp.setTokenId(tokenVO.getTokenId());
                        otp.setOtpCode(input.getActivationCode());
                        otp.setStatus(Constants.ACTIVATED);
                        serviceDao.updateStatusOTP(otp);
                        serviceDao.updateTokenStatus(tokenVO);
                        MfaUtils.setSuccessResponse(output);
                    } else {
                        MfaUtils.setInvalidActivationCodeResponse(output);
                    }
                } else {
                    MfaUtils.setTokenAlreadActivated(output);
                }
            } else {
                MfaUtils.setTokenDoesntExists(output);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            MfaUtils.setSystemError(output, e);
            return output;
        }
    }
    
    /**
     * This method is used to re-send the activation code.
     * 
     * @param channel
     * @param category
     * @param tokenId
     * @return
     */
    @WebMethod
    public MfaResponseVO resendActivationCode(@WebParam(name = "channel") String channel, @WebParam(name = "category") String category, @XmlElement(required = true, nillable = false) @WebParam(name = "tokenId") String tokenId) {
        System.out.println("MFA resendActivationCode");
        MfaResponseVO output = new MfaResponseVO();
        try {
            MfaRequestVO input = new MfaRequestVO(channel, category, tokenId, null, null);
            MfaTokenVO tokenVO = new MfaTokenVO();
            tokenVO.setTokenId(input.getTokenId());
            tokenVO.setChannel(input.getChannel());
            tokenVO.setCategory(input.getCategory());
            if (StringUtils.isEmpty(tokenVO.getChannel())) {
                tokenVO.setChannel("DEFAULT");
            }
            if (StringUtils.isEmpty(tokenVO.getCategory())) {
                tokenVO.setCategory("DEFAULT");
            }
            MfaUtils.validateTokenId(input);
            if (serviceDao.tokenExists(tokenVO)) {
                if (!serviceDao.tokenActivated(tokenVO)) {
                    String activationCode = serviceDao.getActivationCode(tokenVO);
                    output.setActivationOTPCode(activationCode);
                    MfaUtils.setSuccessResponse(output);
                } else {
                    MfaUtils.setTokenAlreadActivated(output);
                }
            } else {
                MfaUtils.setTokenDoesntExists(output);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            MfaUtils.setSystemError(output, e);
            return output;
        }
    }
    
    /**
     * This method is used to send the OTP code.
     * @param channel
     * @param category
     * @param tokenId
     * @return
     */
    @WebMethod
    public MfaResponseVO sendOTP(@WebParam(name = "channel") String channel, @WebParam(name = "category") String category, @XmlElement(required = true, nillable = false) @WebParam(name = "tokenId") String tokenId) {
        System.out.println("MFA sendOTP");
        MfaResponseVO output = new MfaResponseVO();
        try {
            MfaRequestVO input = new MfaRequestVO(channel, category, tokenId, null, null);
            MfaOTPVO otpVO = new MfaOTPVO();
            otpVO.setTokenId(input.getTokenId());
            otpVO.setChannel(input.getChannel());
            otpVO.setCategory(input.getCategory());
            if (StringUtils.isEmpty(otpVO.getChannel())) {
                otpVO.setChannel("DEFAULT");
            }
            if (StringUtils.isEmpty(otpVO.getCategory())) {
                otpVO.setCategory("DEFAULT");
            }
            MfaUtils.validateTokenId(input);
            if (serviceDao.tokenExists(MfaUtils.convert(otpVO))) {
                if (serviceDao.tokenActivated(MfaUtils.convert(otpVO))) {
                    String otpCode = MfaUtils.generateRandomNumber();
                    otpVO.setOtpCode(otpCode);
                    otpVO.setType("O");
                    serviceDao.insertOTP(otpVO);
                    output.setActivationOTPCode(otpCode);
                    MfaUtils.setSuccessResponse(output);
                } else {
                    MfaUtils.setTokenNotActivated(output);
                }
            } else {
                MfaUtils.setTokenDoesntExists(output);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            MfaUtils.setSystemError(output, e);
            return output;
        }
    }
    
    /**
     * This method will validate if the OTP code sent before is valid.
	 * OTP code remains active.
	 * 
     * @param channel
     * @param category
     * @param tokenId
     * @param otpCode
     * @return
     */
    @WebMethod
    public MfaResponseVO validateOTP(@WebParam(name = "channel") String channel, @WebParam(name = "category") String category, @XmlElement(required = true, nillable = false) @WebParam(name = "tokenId") String tokenId, @XmlElement(required = true, nillable = false) @WebParam(name = "otpCode") String otpCode) {
        System.out.println("MFA validateOTP");
        MfaResponseVO output = new MfaResponseVO();
        try {
            MfaRequestVO input = new MfaRequestVO(channel, category, tokenId, otpCode, null);
            MfaOTPVO otpVO = new MfaOTPVO();
            otpVO.setTokenId(input.getTokenId());
            otpVO.setChannel(input.getChannel());
            otpVO.setCategory(input.getCategory());
            if (StringUtils.isEmpty(otpVO.getChannel())) {
                otpVO.setChannel("DEFAULT");
            }
            if (StringUtils.isEmpty(otpVO.getCategory())) {
                otpVO.setCategory("DEFAULT");
            }
            MfaUtils.validateTokenId(input);
            MfaUtils.validateOTPCode(input);
            if (serviceDao.tokenExists(MfaUtils.convert(otpVO))) {
                if (serviceDao.tokenActivated(MfaUtils.convert(otpVO))) {
                    otpVO.setOtpCode(input.getOtpCode());
                    boolean isOtpValid = serviceDao.validateOTPCode(otpVO);
                    if (isOtpValid) {
                        MfaUtils.setSuccessResponse(output);
                    } else {
                        MfaUtils.setInvalidOTPCodeResponse(output);
                    }
                } else {
                    MfaUtils.setTokenNotActivated(output);
                }
            } else {
                MfaUtils.setTokenDoesntExists(output);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            MfaUtils.setSystemError(output, e);
            return output;
        }
    }
    
    /**
     * 
     * This method will authenticate the OTP code. After the authentication, the code will be discarded. 
	 * 
     * @param channel
     * @param category
     * @param tokenId
     * @param otpCode
     * @return
     */
    @WebMethod
    public MfaResponseVO authenticateOTP(@WebParam(name = "channel") String channel, @WebParam(name = "category") String category, @XmlElement(required = true, nillable = false) @WebParam(name = "tokenId") String tokenId, @XmlElement(required = true, nillable = false) @WebParam(name = "otpCode") String otpCode) {
        System.out.println("MFA authenticateOTP");
        MfaResponseVO output = new MfaResponseVO();
        try {
            MfaRequestVO input = new MfaRequestVO(channel, category, tokenId, otpCode, null);
            MfaOTPVO otpVO = new MfaOTPVO();
            otpVO.setTokenId(input.getTokenId());
            otpVO.setChannel(input.getChannel());
            otpVO.setCategory(input.getCategory());
            if (StringUtils.isEmpty(otpVO.getChannel())) {
                otpVO.setChannel("DEFAULT");
            }
            if (StringUtils.isEmpty(otpVO.getCategory())) {
                otpVO.setCategory("DEFAULT");
            }
            MfaUtils.validateTokenId(input);
            MfaUtils.validateOTPCode(input);
            if (serviceDao.tokenExists(MfaUtils.convert(otpVO))) {
                if (serviceDao.tokenActivated(MfaUtils.convert(otpVO))) {
                    otpVO.setOtpCode(input.getOtpCode());
                    boolean isOtpValid = serviceDao.validateOTPCode(otpVO);
                    if (isOtpValid) {
                        otpVO.setStatus(Constants.AUTHENTICATED);
                        serviceDao.updateStatusOTP(otpVO);
                        MfaUtils.setSuccessResponse(output);
                    } else {
                        MfaUtils.setInvalidOTPCodeResponse(output);
                    }
                } else {
                    MfaUtils.setTokenNotActivated(output);
                }
            } else {
                MfaUtils.setTokenDoesntExists(output);
            }
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            MfaUtils.setSystemError(output, e);
            return output;
        }
    }
    
    private boolean validateActivationCode(MfaRequestVO input, MfaTokenVO tokenVO) throws Exception {
        boolean validActivationCode = false;
        String activationCode = serviceDao.getActivationCode(tokenVO);
        if (input.getActivationCode().equalsIgnoreCase(activationCode)) {
            validActivationCode = true;
        }
        return validActivationCode;
    }
}
