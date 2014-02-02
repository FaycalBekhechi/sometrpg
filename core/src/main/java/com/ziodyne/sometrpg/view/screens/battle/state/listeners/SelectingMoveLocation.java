package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import au.com.ds.ef.err.LogicViolationError;
import com.badlogic.gdx.Gdx;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.UnitMovementController;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import org.omg.CosNaming._NamingContextExtStub;

import java.util.Set;

/**
 * Logic for entering and exiting the state where the user is
 * selecting a location to move a unit.
 */
public class SelectingMoveLocation extends FlowListener<BattleContext> {
  private final BattleScreen battle;
  private UnitMovementController movementController;

  public SelectingMoveLocation(BattleScreen screen) {
    super(BattleState.SELECTING_MOVE_LOCATION);
    this.battle = screen;
  }

  @Override
  public void onLeave(BattleContext context) throws LogicViolationError {
    context.mapController.enable();
    battle.hideMoveRange();
    Gdx.input.setInputProcessor(null);
  }

  @Override
  public void onEnter(BattleContext context) throws LogicViolationError {
    context.mapController.disable();
    Set<GridPoint2> bounds = battle.showMoveRange(context.selectedCombatant);
    movementController = new UnitMovementController(battle.getCamera(), bounds, context);
    Gdx.input.setInputProcessor(movementController);
  }
}
