package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;
import com.ziodyne.sometrpg.view.input.BattleMapController;
import com.ziodyne.sometrpg.view.screens.battle.BattleScreen;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

/**
 * Logic for entering and exiting the initial player turn state
 */
public class PlayerTurnListener extends FlowListener<BattleContext> {
  private final OrthographicCamera camera;

  private final BattleScreen screen;

  private final BattleMapController.Factory controllerFactory;

  private final Pathfinder<GridPoint2> pathfinder;

  private BattleMapController controller;

  public interface Factory {
    public PlayerTurnListener create(OrthographicCamera camera, BattleScreen screen, Pathfinder<GridPoint2> pathfinder);
  }

  @AssistedInject
  public PlayerTurnListener(@Assisted OrthographicCamera camera, @Assisted BattleScreen screen,
                            @Assisted Pathfinder<GridPoint2> pathfinder, BattleMapController.Factory controllerFactory) {
    super(BattleState.PLAYER_TURN);
    this.camera = camera;
    this.screen = screen;
    this.controllerFactory = controllerFactory;
    this.pathfinder = pathfinder;
  }

  @Override
  public void onEnter(BattleContext context) {
    // Check if it still should be the player's turn.
    // If not, trigger friendly_actions_exhausted
    context.mapController = controllerFactory.create(camera, screen, context, pathfinder);
    Gdx.input.setInputProcessor(context.mapController);
  }

  @Override
  public void onLeave(BattleContext context) {
    Gdx.input.setInputProcessor(null);
  }
}
