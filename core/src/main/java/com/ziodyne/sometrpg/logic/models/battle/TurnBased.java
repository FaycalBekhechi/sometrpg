package com.ziodyne.sometrpg.logic.models.battle;

public interface TurnBased {
  public int getTurnNumber();
  public void endTurn();
  public boolean isTurnComplete();
}
