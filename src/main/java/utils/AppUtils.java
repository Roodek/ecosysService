package utils;

import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Game;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static GameDto entityToDto(Game game) {
        return  new GameDto(game.getId(),game.getPlayers(),game.getCardStack());
    }

    // Convert ProductDto to Product entity
    public static Game dtoToEntity(GameDto gameDto) {
        Game product = new Game();
        BeanUtils.copyProperties(gameDto, product);
        return product;
    }
}
