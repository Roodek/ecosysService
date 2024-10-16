package com.eco.ecosystem.controllers.responseObjects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@Setter
@Getter
@NoArgsConstructor
public class BasicGameResponse {
    private UUID gameID;
    private List<UUID> playerIdList;
}
