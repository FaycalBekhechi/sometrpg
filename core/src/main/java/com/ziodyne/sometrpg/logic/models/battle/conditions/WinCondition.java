package com.ziodyne.sometrpg.logic.models.battle.conditions;

import com.ziodyne.sometrpg.logic.models.battle.SomeTRPGBattle;

/**
 * A WinCondition is a predicate that determines if a Battle has been completed or failed by the player.
 */
public interface WinCondition {
  public boolean isFulfilled(SomeTRPGBattle battle);
  public boolean isFailed(SomeTRPGBattle battle);
}
