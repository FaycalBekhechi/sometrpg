package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import aurelienribon.tweenengine.TweenManager;
import com.google.common.base.Optional;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.UnitMover;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * Logic for entering and exiting the state where a unit is moving
 */
public class UnitMoving extends FlowListener<BattleContext> {
  private final BattleScreen battleScreen;
  private final Pathfinder<GridPoint2> pathfinder;
  private final BattleMap map;
  private final TweenManager tweenManager;
  private final float gridSize;
  private final UnitMover mover;

  public UnitMoving(BattleScreen screen, Pathfinder<GridPoint2> pathfinder, BattleMap map, float gridSize, TweenManager tweenManager, UnitMover unitMover) {
    super(BattleState.UNIT_MOVING);
    this.battleScreen = screen;
    this.pathfinder = pathfinder;
    this.tweenManager = tweenManager;
    this.map = map;
    this.gridSize = gridSize;
    this.mover = unitMover;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(final BattleContext context) {
    GridPoint2 start = battleScreen.getCombatantPosition(context.selectedCombatant);
    context.selectedSquare = context.movementDestination;

    final Optional<Path<GridPoint2>> path = pathfinder.computePath(start, context.movementDestination);

    mover.moveCombatant(context.selectedCombatant, path.get(), new UnitMover.MovedCallback() {
      @Override
      public void call() {

        battleScreen.moveCombatant(context.selectedCombatant, context.movementDestination, path.get());
        context.safeTrigger(BattleEvent.UNIT_MOVED);
      }
    });
  }

}
