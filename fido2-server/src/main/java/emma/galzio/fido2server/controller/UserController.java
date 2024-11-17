package emma.galzio.fido2server.controller;


import emma.galzio.fido2server.service.UserLoginService;
import emma.galzio.fido2server.transferObject.UserLoginDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fido2/user")
@RequiredArgsConstructor
public class UserController {

    private final UserLoginService userLoginService;


    @GetMapping("/{session_id}")
    public UserLoginDto getLoggedInUser(@PathVariable("session_id") String sessioId, HttpServletRequest request) {

        String rpId = request.getHeader("rpId");

        return userLoginService.getLoggedUser(sessioId, rpId);

    }

}
