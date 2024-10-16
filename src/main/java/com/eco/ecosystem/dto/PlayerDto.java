package com.eco.ecosystem.dto;

import com.eco.ecosystem.entities.PlayerCard;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlayerDto {

    UUID playerID;
    List<List<PlayerCard>> board;
}
