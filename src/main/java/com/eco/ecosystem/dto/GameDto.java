package com.eco.ecosystem.dto;

import com.eco.ecosystem.entities.Player;
import com.eco.ecosystem.entities.PlayerCard;

import java.util.List;
import java.util.UUID;


public record GameDto(
        UUID id,
        List<Player>players,
        List<PlayerCard> cardStack) { }
