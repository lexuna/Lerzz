package de.lexuna.lerzz.server.controller;


import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.UserAlreadyExistException;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegistrationController {
    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("password") String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        if (!password.equals(confirmPassword)) { //TODO in den service verschieben
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }
        user.setPassword(password);
        try {
            userService.registerNewUserAccount(user);
        } catch (UserAlreadyExistException e) {
            e.printStackTrace();
            model.addAttribute("error", "email already exists.");
            return "register";
        }
        return "redirect:/login";
    }
}