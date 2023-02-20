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

@Controller
@Slf4j
public class SessionController {

//    @GetMapping({"/", "/index", "/home", "/login"})
//    public String process(Model model,HttpServletRequest request) {
//        @SuppressWarnings("unchecked")
//        List<String> messages = (List<String>) request.getSession().getAttribute("MY_SESSION_MESSAGES");
//        if (messages == null) {
//            log.info("add new session");
//            messages = new ArrayList<>();
//            request.getSession().setAttribute("MY_SESSION_MESSAGES", messages);
//        }
//
//        model.addAttribute("sessionMessages", messages);
//        return "";
//    }


    @PostMapping("/logout")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/";
    }

}