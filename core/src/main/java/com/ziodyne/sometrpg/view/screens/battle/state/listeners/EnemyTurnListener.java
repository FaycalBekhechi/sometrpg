package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.TurnBased;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.rendering.LiveEntity;
import com.ziodyne.sometrpg.view.rendering.TextRenderer;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class EnemyTurnListener extends FlowListener<BattleContext> {

  private TurnBased turnBased;
  private TextRenderer textRenderer;
  private final Engine engine;

  public EnemyTurnListener(TurnBased turnBased, Engine engine, TextRenderer textRenderer) {

    super(BattleState.ENEMY_TURN);
    this.turnBased = turnBased;
    this.textRenderer = textRenderer;
    this.engine = engine;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
    LiveEntity text = textRenderer.renderViewportText("ENEMY TURN", new Vector2(800, 450), TextRenderer.Size.SIXTYFOUR);
    TimedProcess timedProcess = new TimedProcess(() -> {
      text.dispose();
      turnBased.endTurn();
      context.safeTrigger(BattleEvent.ENEMY_TURN_FINISH);
    }, 1500);

    Entity timer = new Entity();
    timer.add(timedProcess);

    engine.addEntity(timer);
  }
}
