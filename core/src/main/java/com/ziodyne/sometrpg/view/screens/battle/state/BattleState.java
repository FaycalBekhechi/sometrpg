package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.StateEnum;

/**
 * These are the states of a battle with regards the user input.
 */
public enum BattleState implements StateEnum {
  ENEMY_TURN,
  SELECTING_UNIT_ACTION,
  SELECTING_MOVE_LOCATION,
  PLAYER_TURN,
  UNIT_MOVING,
  UNIT_ATTACKING,
  AWAITING_ATTACK_CONFIRMATION,
  SELECTING_ATTACK_TARGET,
  SHOWING_FRIENDLY_DETAILS,
  SHOWING_ENEMY_DETAILS,
  END_SCREEN
}
