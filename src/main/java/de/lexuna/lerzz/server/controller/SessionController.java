package de.lexuna.lerzz.server.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for handling user sessions.
 */
@Controller
@Slf4j
public class SessionController {

    /**
     * Process GET requests for the index, home, and login pages.
     * If there is an existing session, retrieve any session messages that have been saved to it.
     * Otherwise, create a new session and initialize it with an empty list of messages.
     *
     * @param model   The model to which session messages will be added
     * @param request The HTTP servlet request, which is used to retrieve and create the session
     * @return The name of the template to be rendered
     */
    @GetMapping({"/", "/index", "/home", "/login"})
    public String process(Model model,HttpServletRequest request) {
        @SuppressWarnings("unchecked")
        List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
        if (messages == null) {
            log.info("add new session");
            messages = new ArrayList<>();
            request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
        }

        model.addAttribute("sessionMessages", messages);
        return "home";
    }

    /**
     * Process POST requests for logging out the user.
     * This method invalidates the current session and redirects the user to the home page.
     *
     * @param request The HTTP servlet request, which is used to retrieve and invalidate the session
     * @return A redirect to the home page
     */
    @PostMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

}