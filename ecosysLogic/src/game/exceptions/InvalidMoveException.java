package game.exceptions;

public class InvalidMoveException extends Exception{
    public InvalidMoveException(String message){
        super("Invalid move");
    }
}
