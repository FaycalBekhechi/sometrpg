package com.ziodyne.sometrpg.view.screens.battle.state;

import au.com.ds.ef.EasyFlow;
import au.com.ds.ef.StatefulContext;

import static au.com.ds.ef.FlowBuilder.*;
import static com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent.*;
import static com.ziodyne.sometrpg.view.screens.battle.state.BattleState.*;

public class BattleFlow {

  /**
   * This is the state machine that describes the user-interaction flow
   * during battle.
   */
  private static final EasyFlow<BattleContext> FLOW =
    from(PLAYER_TURN).transit(
      on(FRIENDLY_UNIT_SELECTED).to(SELECTING_UNIT_ACTION).transit(
        on(MOVE_ACTION_SELECTED).to(SELECTING_MOVE_LOCATION).transit(
          on(MOVE_LOC_SELECTED).to(UNIT_MOVING).transit(
            on(UNIT_MOVED).to(PLAYER_TURN)
          )
        ),
        on(ATTACK_SELECTED).to(AWAITING_ATTACK_CONFIRMATION).transit(
          on(ATTACK_CONFIRMED).to(UNIT_ATTACKING).transit(
            on(UNIT_ATTACKED).to(PLAYER_TURN)
          )
        )
      ),
      on(ENEMY_UNIT_SELECTED).to(SHOWING_UNIT_DETAILS).transit(
        on(UNIT_DETAILS_CLOSED).to(PLAYER_TURN)
      ),
      on(FRIENDLY_UNIT_STATS_SELECTED).to(SHOWING_UNIT_DETAILS).transit(
        on(UNIT_DETAILS_CLOSED).to(PLAYER_TURN)
      ),
      on(FRIENDLY_ACTIONS_EXHAUSTED).to(ENEMY_TURN)
    );

  private static class BattleContext extends StatefulContext {

  }

  private BattleFlow() { }
}
