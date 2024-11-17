package emma.galzio.fido2server.service;

import emma.galzio.fido2server.transferObject.UserLoginDto;

public interface UserLoginService {

    UserLoginDto getLoggedUser(String sessionId, String rpId);

}
