package de.lexuna.lerzz.server.controller;


import de.lexuna.lerzz.model.User;
import de.lexuna.lerzz.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The RegistrationController handles HTTP requests related to user registration.
 * It allows a user to register a new account by filling out a form with their email and password.
 * If the registration is successful, the user is redirected to the login page.
 * If the registration fails, the user is redirected to the registration page with an error message.
 */
@Controller
public class RegistrationController {

    /**
     * The UserService to be used for registering new users.
     */
    @Autowired
    private UserService userService;

    /**
     * Displays the registration form.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered, which is "register".
     */
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    /**
     * Handles the submission of the registration form.
     * @param user The User object representing the user being registered.
     * @param password The user's chosen password.
     * @param confirmPassword The user's confirmation of their chosen password.
     * @param model The model to be used for rendering the view.
     * @return The name of the view to be rendered, which is either "register" (with an error message)
     * or "redirect:/login" (on successful registration).
     */
    @PostMapping("/register")
    public String register(@ModelAttribute User user,
                           @RequestParam("password") String password,
                           @RequestParam("confirmPassword") String confirmPassword,
                           Model model) {
        if (!password.equals(confirmPassword)) { //TODO in den service verschieben
            model.addAttribute("error", "Passwords do not match.");
            return "register";
        }
//        user.setPassword(password);
        try {
            userService.registerNewUserAccount(user);
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "email already exists.");
            return "register";
        }
        return "redirect:/login";
    }
}
