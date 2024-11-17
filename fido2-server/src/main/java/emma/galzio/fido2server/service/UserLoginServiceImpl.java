package emma.galzio.fido2server.service;

import emma.galzio.fido2server.common.PublicKeyCredentialRpEntity;
import emma.galzio.fido2server.common.server.RegOptionResponse;
import emma.galzio.fido2server.common.server.ServerPublicKeyCredentialUserEntity;
import emma.galzio.fido2server.model.Session;
import emma.galzio.fido2server.model.User;
import emma.galzio.fido2server.model.UserKey;
import emma.galzio.fido2server.transferObject.UserLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserLoginServiceImpl implements UserLoginService {

    private final SessionService sessionService;
    private final UserKeyService userKeyService;


    @Override
    public UserLoginDto getLoggedUser(String sessionId, String rpId) {

        Session session = sessionService.getSession(sessionId);

        log.info("Id de sesion: {}", sessionId);
        log.info("Sesion: {}", session);


        RegOptionResponse regOptionResponse = session.getRegOptionResponse();
        //Probar user acá
        //ServerPublicKeyCredentialUserEntity user = regOptionResponse.getUser();
        PublicKeyCredentialRpEntity rp = regOptionResponse.getRp();

        if(!rpId.equals(rp.getId())){
            //TODO threat invalid rpID
            return null;
        }
        UserLoginDto userLoginDto = new UserLoginDto();
        if(session.getUser() == null) return null;
        User user = session.getUser();
        userLoginDto.setId(user.getEmail());
        userLoginDto.setUsername(user.getUsername());
        userLoginDto.setDisplayName(user.getFirstName() + " " + user.getLastName());
        //TODO threat get user from ID
        /*List<UserKey> withUserId = userKeyService.getWithUserId(rpId, user.getId());
        UserKey userKey = withUserId.get(0);

        userLoginDto.setId(userKey.getId());
        userLoginDto.setUsername(userKey.getName());
        userLoginDto.setIcon(userKey.getIcon());
        userLoginDto.setDisplayName(userKey.getDisplayName());*/

        return userLoginDto;
    }
}
