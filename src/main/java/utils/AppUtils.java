package utils;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Game;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static GameDto gameEntityToDto(Game game) {
        return  new GameDto(game.getId(),game.getPlayers(),game.getCardStack());
    }

    // Convert ProductDto to Product entity
    public static Game gameDtoToEntity(GameDto gameDto) {
        Game game = new Game();
        BeanUtils.copyProperties(gameDto, game);
        return game;
    }
}
