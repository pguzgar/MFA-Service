# MFA-Service

Configure the port where the service will be exposed in `applicationContext.xml`

WSDL URL Example:
######`http://localhost:8180/mfa/MFAService?wsdl`


Methods exposed:

###### registerToken
* This method is used to register a token and immediately get an activation code.
* Token will be marked as registered

###### activateToken
* This method is used to activate the token previously registered.
* Token will be marked as activated

###### resendActivationCode
* This method is used to re-send the activation code.

###### sendOTP
* This method is used to send an OTP code.

###### validateOTP
* This method will validate if the OTP code sent previously is valid.
* OTP code remains active.

###### authenticateOTP
* This method will authenticate the OTP code. 
* After the authentication, the code will be discarded. 
