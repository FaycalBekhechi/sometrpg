package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.components.TimedProcess;
import com.ziodyne.sometrpg.view.rendering.LiveEntity;
import com.ziodyne.sometrpg.view.rendering.TextRenderer;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class EndScreenListener extends FlowListener<BattleContext> implements Logged {

  private static final String YOU_WON_LABEL = "YOU DEFEATED";
  private static final String YOU_LOST_LABEL = "YOU DIED";
  private static final long ENDGAME_TEXT_TTL = 5000;

  private final Engine engine;
  private final Viewport viewport;
  private final TextRenderer textRenderer;

  public EndScreenListener(Engine engine, Viewport viewport, TextRenderer textRenderer) {
    super(BattleState.END_SCREEN);

    this.engine = engine;
    this.viewport = viewport;
    this.textRenderer = textRenderer;
  }

  @Override
  public void onLeave(BattleContext context) {

  }

  @Override
  public void onEnter(BattleContext context) {
    Vector2 screenCenter = new Vector2(viewport.getViewportWidth()/2, viewport.getViewportHeight()/2);

    LiveEntity text;
    if (context.won) {
      logDebug("Battle won!");
      text = textRenderer.renderCenteredViewportText(YOU_WON_LABEL, screenCenter, TextRenderer.Size.SIXTYFOUR);
    } else {
      logDebug("Battle lost");
      text = textRenderer.renderCenteredViewportText(YOU_LOST_LABEL, screenCenter, TextRenderer.Size.SIXTYFOUR);
    }

    TimedProcess timedProcess = new TimedProcess(text::dispose, ENDGAME_TEXT_TTL);

    Entity timer = new Entity();
    timer.add(timedProcess);
    engine.addEntity(timer);
  }
}
