package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleAction;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatResolver;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatSummary;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.input.InputHandlerStack;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.widgets.CombatForecast;

/**
 * A handler for the state where a the user must confirm a preview of an action.
 */
public class AttackConfirmationListener extends FlowListener<BattleContext> {
  private final Engine engine;
  private final EntityFactory entityFactory;
  private final OrthographicCamera camera;
  private final float gridSize;
  private final CombatResolver resolver;
  private final InputHandlerStack handlerStack;
  private CombatForecast forecast;

  public AttackConfirmationListener(Engine engine, EntityFactory entityFactory, OrthographicCamera camera,
                                    CombatResolver combatResolver, InputHandlerStack handlers, float gridSize) {
    super(BattleState.AWAITING_ATTACK_CONFIRMATION);
    this.engine = engine;
    this.handlerStack = handlers;
    this.entityFactory = entityFactory;
    this.camera = camera;
    this.resolver = combatResolver;
    this.gridSize = gridSize;
  }

  @Override
  public void onLeave(BattleContext context) {

    if (forecast != null) {
      forecast.dispose();
    }

    context.mapController.enable();
    handlerStack.pop();
  }

  @Override
  public void onEnter(final BattleContext context) {
    Optional<BattleAction> action = context.getAction();
    Preconditions.checkState(action.isPresent(), "Attack confirmation requested with no attack to perform.");

    CombatSummary summary = resolver.preview(action.get());

    // Center the radial menu on the center of the selected unit's square.
    float x = context.selectedSquare.x * gridSize + (gridSize / 2);
    float y = context.selectedSquare.y * gridSize + (gridSize / 2);

    forecast = new CombatForecast(engine, entityFactory, new Vector2(x, y), camera, summary);
    forecast.addSelectedHandler((result) -> {
      if (result == CombatForecast.Action.CONFIRM) {
        context.safeTrigger(BattleEvent.ATTACK_CONFIRMED);
      } else {
        context.safeTrigger(BattleEvent.ATTACK_CANCEL);
      }
    });
    forecast.render();

    context.mapController.disable();
    handlerStack.push(forecast);
  }
}
