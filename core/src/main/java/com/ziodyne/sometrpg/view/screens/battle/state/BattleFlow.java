package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.EasyFlow;

import static au.com.ds.ef.FlowBuilder.*;
import static com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent.*;
import static com.ziodyne.sometrpg.view.screens.battle.state.BattleState.*;

public class BattleFlow {

  /**
   * This is the state machine that describes the user-interaction flow
   * during battle.
   */
  public static final EasyFlow<BattleContext> FLOW =
    from(PLAYER_TURN).transit(
      on(FRIENDLY_UNIT_SELECTED).to(SELECTING_UNIT_ACTION).transit(
        on(ACTIONS_EXHAUSTED).to(PLAYER_TURN),
        on(FRIENDLY_UNIT_SELECTION_CANCEL).to(PLAYER_TURN),
        on(INFO_ACTION_SELECTED).to(SHOWING_UNIT_DETAILS),
        on(MOVE_ACTION_SELECTED).to(SELECTING_MOVE_LOCATION).transit(
          on(MOVE_LOC_SELECTED).to(UNIT_MOVING).transit(
            on(UNIT_MOVED).to(SELECTING_UNIT_ACTION)
          ),
          on(MOVE_ACTION_CANCEL).to(PLAYER_TURN)
        ),
        on(ATTACK_ACTION_SELECTED).to(SELECTING_ATTACK_TARGET).transit(
          on(TARGET_SELECTED).to(AWAITING_ATTACK_CONFIRMATION).transit(
            on(ATTACK_CONFIRMED).to(UNIT_ATTACKING).transit(
              on(UNIT_ATTACKED).to(SELECTING_UNIT_ACTION)
            ),
            on(ATTACK_CANCEL).to(SELECTING_ATTACK_TARGET)
          ),
          on(ATTACK_CANCEL).to(PLAYER_TURN)
        )
      ),
      on(ENEMY_UNIT_SELECTED).to(SHOWING_UNIT_DETAILS).transit(
        on(UNIT_DETAILS_CLOSED).to(PLAYER_TURN)
      ),
      on(FRIENDLY_UNIT_STATS_SELECTED).to(SHOWING_UNIT_DETAILS),
      on(FRIENDLY_ACTIONS_EXHAUSTED).to(ENEMY_TURN).transit(
        on(ENEMY_TURN_FINISH).to(PLAYER_TURN)
      ),
      on(BATTLE_LOST).finish(END_SCREEN),
      on(BATTLE_WON).finish(END_SCREEN)
    ).trace();

  private BattleFlow() { }
}
