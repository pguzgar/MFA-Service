package mx.daro.mfa.service.business.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import mx.daro.mfa.service.vo.MfaOTPVO;
import mx.daro.mfa.service.vo.MfaTokenVO;

@Repository
public class MfaServiceDAO {
	
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Value( "${token.expiration}" )
    private String tokenExpirationMinutes;
    
    
    public void insertToken(MfaTokenVO tokenVO) throws Exception {
        jdbcTemplate.update("INSERT INTO MFA_TOKEN (CHANNEL, CATEGORY, TOKEN_ID, STATUS, CREATE_DT, UPDATE_DT) values (?,?,?,?,SYSDATE(),null)", tokenVO.getChannel(), tokenVO.getCategory(), tokenVO.getTokenId(), tokenVO.getStatus());
    }
    
    public void updateTokenStatus(MfaTokenVO tokenVO) throws Exception {
        jdbcTemplate.update("UPDATE MFA_TOKEN set STATUS =?, UPDATE_DT=SYSDATE() where CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ?", tokenVO.getStatus(), tokenVO.getChannel(), tokenVO.getCategory(), tokenVO.getTokenId());
    }
    
    public boolean tokenExists(MfaTokenVO tokenVO) throws Exception {
        Integer r = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MFA_TOKEN where CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ?", Integer.class, tokenVO.getChannel(), tokenVO.getCategory(), tokenVO.getTokenId());
        return r > 0;
    }
    
    public boolean tokenActivated(MfaTokenVO tokenVO) throws Exception {
        Integer r = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM MFA_TOKEN where CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ? and STATUS='A'", Integer.class, tokenVO.getChannel(), tokenVO.getCategory(), tokenVO.getTokenId());
        return r > 0;
    }
    
    public String getActivationCode(MfaTokenVO tokenVO) throws Exception {
        return jdbcTemplate.queryForObject("SELECT OTP_CODE from MFA_OTP where CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ? and TYPE='R' and STATUS='N'", String.class, tokenVO.getChannel(), tokenVO.getCategory(), tokenVO.getTokenId());
    }
    
    public void insertOTP(MfaOTPVO otpVO) throws Exception {
        jdbcTemplate.update("INSERT INTO MFA_OTP (CHANNEL, CATEGORY, TOKEN_ID, OTP_CODE, TYPE, STATUS, CREATE_DT, UPDATE_DT) values (?,?,?,?,?,'N',SYSDATE(),null)", otpVO.getChannel(), otpVO.getCategory(), otpVO.getTokenId(), otpVO.getOtpCode(), otpVO.getType());
    }
    
    public void updateStatusOTP(MfaOTPVO otpVO) throws Exception {
        jdbcTemplate.update("UPDATE MFA_OTP set STATUS =?, UPDATE_DT=SYSDATE() where OTP_CODE=? AND CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ?", otpVO.getStatus(), otpVO.getOtpCode(), otpVO.getChannel(), otpVO.getCategory(), otpVO.getTokenId());
    }
    
    public String getOTPCode(MfaOTPVO otpVO) throws Exception {
        Integer expirationMinutes = Integer.valueOf(tokenExpirationMinutes);
        return jdbcTemplate.queryForObject("SELECT OTP_CODE from MFA_OTP where CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ? and ACTIVE = 'N' and CREATE_DT >= (SYSDATE() - INTERVAL " + expirationMinutes + " MINUTE))", String.class, otpVO.getChannel(), otpVO.getCategory(), otpVO.getTokenId());
    }
    
    public boolean validateOTPCode(MfaOTPVO otpVO) throws Exception {
        Integer expirationMinutes = Integer.valueOf(tokenExpirationMinutes);
        Integer r = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM (SELECT 1 from MFA_OTP where CHANNEL = ? and CATEGORY = ? and TOKEN_ID = ? and STATUS= 'N' and OTP_CODE = ? and CREATE_DT >= (SYSDATE() - INTERVAL " + expirationMinutes + " MINUTE)) data", Integer.class, otpVO.getChannel(), otpVO.getCategory(), otpVO.getTokenId(), otpVO.getOtpCode());
        return r > 0;
    }
}
