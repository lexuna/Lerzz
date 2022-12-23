package de.lexuna.lerzz.server.controller;

import de.lexuna.lerzz.model.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/card")
public class GameController {

    @Autowired
    private GameRepository repo;

}
