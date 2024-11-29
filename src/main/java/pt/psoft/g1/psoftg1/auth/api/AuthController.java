package pt.psoft.g1.psoftg1.auth.api;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequiredArgsConstructor
public class AuthController {

    @GetMapping("/login")
    public String login() {
        return "redirect:/oauth2/authorization/google";
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    // Endpoint de redirecionamento pós-login
    /*@GetMapping("/login/oauth2/success")
    @ResponseBody
    public String loginSuccess() {
        return "Login realizado com sucesso!";
    }
*/
    @GetMapping("/login/oauth2/success")
    public String loginSuccess(OAuth2AuthenticationToken authentication) {
        String email = authentication.getPrincipal().getAttribute("email");
        return "redirect:/home"; // Redireciona para /home após sucesso
    }
}
