package emma.galzio.fido2rp.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hola")
@Slf4j
public class TestController {

    @GetMapping("")
    public String holaMundo() {
        return "test";
    }
}
