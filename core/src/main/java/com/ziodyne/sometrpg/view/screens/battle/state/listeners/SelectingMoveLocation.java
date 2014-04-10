package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.GridSelectionController;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

import java.util.Set;

/**
 * Logic for entering and exiting the state where the user is
 * selecting a location to move a unit.
 */
public class SelectingMoveLocation extends FlowListener<BattleContext> {
  private final BattleScreen battle;
  private GridSelectionController movementController;

  public SelectingMoveLocation(BattleScreen screen) {
    super(BattleState.SELECTING_MOVE_LOCATION);
    this.battle = screen;
  }

  @Override
  public void onLeave(BattleContext context) {
    context.mapController.enable();
    battle.hideMoveRange();
    Gdx.input.setInputProcessor(null);
  }

  @Override
  public void onEnter(final BattleContext context) {
    context.mapController.disable();
    Set<GridPoint2> bounds = battle.showMoveRange(context.selectedCombatant);
    movementController = new GridSelectionController(battle.getCamera(), bounds, new GridSelectionController.SelectionHandler() {
      @Override
      public void handleSelection(GridPoint2 selectedPoint) {
        context.movementDestination = selectedPoint;
        context.safeTrigger(BattleEvent.MOVE_LOC_SELECTED);
      }

      @Override
      public void handleCancelation() {
        context.safeTrigger(BattleEvent.MOVE_ACTION_CANCEL);
      }
    });
    Gdx.input.setInputProcessor(movementController);
  }
}
