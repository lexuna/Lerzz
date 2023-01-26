package de.lexuna.lerzz.server.controller;


import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.model.repository.UserRepository;
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
    private de.lexuna.lerzz.model.repository.UserRepository UserRepository;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User("user@mail", "name", "pw"));
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("password") String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }
        if (UserRepository.findByMail(user.getMail()) != null) {
            model.addAttribute("error", "email already exists.");
            return "register";
        }
        user.setPassword(password);
        UserRepository.save(user);
        return "redirect:/login";
    }
}