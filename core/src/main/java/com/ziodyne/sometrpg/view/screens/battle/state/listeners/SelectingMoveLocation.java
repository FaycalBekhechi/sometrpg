package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.google.common.base.Optional;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
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
  private final float gridSize;
  private GridSelectionController movementController;

  public SelectingMoveLocation(BattleScreen screen, float gridSize) {
    super(BattleState.SELECTING_MOVE_LOCATION);
    this.battle = screen;
    this.gridSize = gridSize;
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
    final Pathfinder<GridPoint2> pathfinder = context.battle.createCachedPathfinder();
    movementController = new GridSelectionController(battle.getCamera(), bounds, gridSize, new GridSelectionController.SelectionHandler() {
      @Override
      public void handleHover(GridPoint2 hoveredPoint) {
        GridPoint2 start = context.battle.getCombatantPosition(context.selectedCombatant);
        Optional<Path<GridPoint2>> path = pathfinder.computePath(start, hoveredPoint);
        if (path.isPresent()) {
          // TODO: draw path guides.
        }
      }

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
