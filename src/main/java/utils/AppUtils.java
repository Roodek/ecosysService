package utils;

import com.eco.ecosystem.controllers.responseObjects.GameResponse;
import com.eco.ecosystem.dto.GameDto;
import com.eco.ecosystem.entities.Game;
import org.springframework.beans.BeanUtils;

public class AppUtils {
    public static GameDto gameEntityToDto(Game game) {
        return  new GameDto(game.getId(),game.getPlayers(),game.getCardStack(), game.getTurn(),game.getCreatedAt());
    }
    public static Game gameDtoToEntity(GameDto gameDto) {
        Game game = new Game();
        BeanUtils.copyProperties(gameDto, game);
        return game;
    }
    public static GameResponse gameDtoToResponse(GameDto gameDto){
        return new GameResponse(gameDto.getId(),gameDto.getPlayers(), gameDto.getTurn());
    }
}
