package emma.galzio.fido2rp.controller;

import emma.galzio.fido2rp.config.FidoServerConfig;
import emma.galzio.fido2rp.transferObject.UserLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/logged-user")
@RequiredArgsConstructor
public class UserController {

    @Value("${fido2.rp.id}")
    private String rpId;
    private String fidoServerHost;
    private String scheme;
    private final String COOKIE_NAME = "fido2-session-id";

    private final RestTemplate restTemplate;
    private final FidoServerConfig fidoServerConfig;



    @GetMapping("")
    public UserLoginDto getUserFromSession(HttpServletRequest httpServletRequest) {

        Cookie[] cookies = httpServletRequest.getCookies();
        if (cookies == null || cookies.length == 0) {
            //error
            return null;
        }

        String sessionId = null;
        for (Cookie cookie : cookies) {
            if (COOKIE_NAME.equals(cookie.getName())) {
                sessionId = cookie.getValue();
                break;
            }
        }

        fidoServerHost = fidoServerConfig.getHost();
        scheme = fidoServerConfig.getScheme();

        //Usar cookies en vez de path

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.newInstance();
        String path = String.format("/fido2/user/%s", sessionId);
        String loggedInUserUri = uriComponentsBuilder
                .scheme(scheme)
                .host(fidoServerHost)
                .port(fidoServerConfig.getPort())
                .path(path)
                .build().toUriString();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Cookie", String.format("%s=%s", COOKIE_NAME, sessionId));
        httpHeaders.add("rpId", rpId);
        HttpEntity<String> request = new HttpEntity<>(httpHeaders);
        ResponseEntity<UserLoginDto> response = restTemplate.exchange(loggedInUserUri, HttpMethod.GET, request, UserLoginDto.class);
        UserLoginDto userLoginDto = response.getBody();
        if (userLoginDto != null) {
            return userLoginDto;
        }

        return null;
    }

}
