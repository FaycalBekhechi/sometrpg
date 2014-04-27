package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.List;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.artemis.Entity;
import com.google.common.base.Optional;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.navigation.PathSegment;
import com.ziodyne.sometrpg.view.navigation.PathUtils;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.tween.TweenUtils;

/**
 * Logic for entering and exiting the state where a unit is moving
 */
public class UnitMoving extends FlowListener<BattleContext> {
  private final BattleScreen battleScreen;
  private final Pathfinder<GridPoint2> pathfinder;
  private final BattleMap map;
  private final TweenManager tweenManager;

  public UnitMoving(BattleScreen screen, Pathfinder<GridPoint2> pathfinder, BattleMap map, TweenManager tweenManager) {
    super(BattleState.UNIT_MOVING);
    this.battleScreen = screen;
    this.pathfinder = pathfinder;
    this.tweenManager = tweenManager;
    this.map = map;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(final BattleContext context) {
    GridPoint2 combatantLoc = map.getCombatantPosition(context.selectedCombatant);
    context.selectedSquare = context.movementDestination;

    com.ziodyne.sometrpg.logic.models.Character character = context.selectedCombatant.getCharacter();
    final Entity entity = battleScreen.getUnitEntity(character);

    Position position = entity.getComponent(Position.class);
    if (position != null) {
      Timeline movement = Timeline.createSequence();
      Optional<Path<GridPoint2>> path = pathfinder.computePath(combatantLoc, context.selectedSquare);
      if (path.isPresent()) {
        movement = TweenUtils.moveAlongPath(path.get(), .3f, position, 1);
      }

      movement.setCallback(new TweenCallback() {
        @Override
        public void onEvent(int i, BaseTween<?> baseTween) {
          context.safeTrigger(BattleEvent.UNIT_MOVED);
        }
      })
      .start(tweenManager);
    }

    battleScreen.moveCombatant(context.selectedCombatant, context.movementDestination);
  }
}
