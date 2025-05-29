package sfu.cp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sfu.cp.model.User;
import sfu.cp.service.UserService;

import java.util.Map;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/", "/auth"})
    public String showAuthPage() {
        return "auth";
    }

    @PostMapping("/register")
    public String register(@RequestParam Map<String, String> params, Model model) {
        if (!params.get("password").equals(params.get("confirm"))) {
            model.addAttribute("error", "Passwords don't match");
            return "auth";
        }
        User user = new User();
        user.setFio(params.get("fio"));
        user.setPhone(params.get("phone"));
        user.setEmail(params.get("email"));
        user.setSeriesNumber(params.get("seriesNumber"));
        user.setIssuedBy(params.get("issuedBy"));
        user.setIssuedDate(params.get("issuedDate"));
        user.setRegistrationPlace(params.get("registrationPlace"));
        user.setPassword(params.get("password"));
        userService.register(user);
        return "redirect:/auth";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        boolean isAuthenticated = userService.authenticate(username, password);
        return isAuthenticated ? "redirect:/home" : "redirect:/auth?error";
    }
}
