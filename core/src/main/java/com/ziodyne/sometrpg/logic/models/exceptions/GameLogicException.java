package com.ziodyne.sometrpg.logic.models.exceptions;

/**
 * Should be thrown when a user of the game model attempts to violate the rules of the game.
 *
 * Example: The caller tries to move a unit to a square that is already occupied. Or from a square that has no units.
 */
public class GameLogicException extends RuntimeException {
  public GameLogicException(String message) {
    super(message);
  }
}
