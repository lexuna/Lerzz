package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.modle.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class GameController {

    @Autowired
    private GameRepository repo;

}
