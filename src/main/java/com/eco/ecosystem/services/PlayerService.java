package com.eco.ecosystem.services;

import com.eco.ecosystem.entities.Player;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    public List<Player> getStudents() {
        return List.of(new Player(1L, "mark"));
    }

}
