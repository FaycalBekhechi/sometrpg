package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.logic.models.battle.TurnBased;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.rendering.LiveEntity;
import com.ziodyne.sometrpg.view.rendering.TextRenderer;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleEvent;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

import static com.ziodyne.sometrpg.view.rendering.TextRenderer.Size.SIXTYFOUR;

public class EnemyTurnListener extends FlowListener<BattleContext> {
  private static final String ENEMY_TURN_LABEL = "ENEMY TURN";
  private static final long ENEMY_TURN_LABEL_DURATION = 1500L;

  private final TurnBased turnBased;
  private final TextRenderer textRenderer;
  private final Engine engine;
  private final Viewport viewport;

  public EnemyTurnListener(TurnBased turnBased, Engine engine, Viewport viewport, TextRenderer textRenderer) {

    super(BattleState.ENEMY_TURN);
    this.viewport = viewport;
    this.turnBased = turnBased;
    this.textRenderer = textRenderer;
    this.engine = engine;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
    // Render the enemy turn banner at the center of the screen
    Vector2 screenCenter = new Vector2(viewport.getViewportWidth()/2, viewport.getViewportHeight()/2);
    LiveEntity text = textRenderer.renderViewportText(ENEMY_TURN_LABEL, screenCenter, SIXTYFOUR);

    // Dispose of the banner in ENEMY_TURN_LABEL_DURATION milliseconds
    TimedProcess timedProcess = new TimedProcess(() -> {
      text.dispose();
      turnBased.endTurn();
      context.safeTrigger(BattleEvent.ENEMY_TURN_FINISH);
    }, ENEMY_TURN_LABEL_DURATION);

    Entity timer = new Entity();
    timer.add(timedProcess);

    engine.addEntity(timer);
  }
}
