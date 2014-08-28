package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.common.eventbus.EventBus;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.events.TurnStarted;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.TurnBased;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * Logic for entering and exiting the initial player turn state
 */
public class PlayerTurnListener<T extends Battle & TurnBased> extends FlowListener<BattleContext> {
  private final OrthographicCamera camera;

  private final BattleScreen screen;

  private final BattleMapController.Factory controllerFactory;

  private final Pathfinder<GridPoint2> pathfinder;

  private final float gridSize;

  private final T battle;

  private BattleMapController controller;

  private final EventBus eventBus;

  @AssistedInject
  public PlayerTurnListener(OrthographicCamera camera, BattleScreen screen, T battle, Pathfinder<GridPoint2> pathfinder,
                              float gridSize, BattleMapController.Factory controllerFactory, EventBus eventBus) {

    super(BattleState.PLAYER_TURN);
    this.camera = camera;
    this.screen = screen;
    this.controllerFactory = controllerFactory;
    this.pathfinder = pathfinder;
    this.gridSize = gridSize;
    this.battle = battle;
    this.eventBus = eventBus;
  }

  @Override
  public void onEnter(BattleContext context) {
    // Check if it still should be the player's turn.
    // If not, trigger friendly_actions_exhausted

    eventBus.post(new TurnStarted());

    if (battle.isWon()) {
      context.won = true;
      context.safeTrigger(BattleEvent.BATTLE_WON);
    } else if (battle.isLost()) {
      context.won = false;
      context.safeTrigger(BattleEvent.BATTLE_LOST);
    } else if (battle.isTurnComplete()) {
      battle.endTurn();
      context.safeTrigger(BattleEvent.FRIENDLY_ACTIONS_EXHAUSTED);
    } else {

      context.mapController = controllerFactory.create(camera, screen, context, pathfinder, gridSize);
      Gdx.input.setInputProcessor(context.mapController);
    }
  }

  @Override
  public void onLeave(BattleContext context) {
    Gdx.input.setInputProcessor(null);
  }
}
