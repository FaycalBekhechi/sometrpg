package com.ziodyne.sometrpg.view.widgets;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatSummary;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantBattleResult;
import com.ziodyne.sometrpg.logic.util.MathUtils;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

public class CombatForecast extends Widget {
  private static final String CANCEL_MENU_ID = "cancel";
  private static final String SELECT_MENU_ID = "select";

  private final Vector2 position;
  private final EntityFactory entityFactory;
  private final RadialMenu menu;
  private final CombatSummary summary;

  public static enum Action {
    CONFIRM,
    REJECT
  }

  private static enum ResultSide {
    LEFT,
    RIGHT
  }

  public CombatForecast(Engine engine, EntityFactory entityFactory, Vector2 position, OrthographicCamera camera,
                        CombatSummary summary) {
    super(engine);

    this.position = position;
    this.entityFactory = entityFactory;
    this.summary = summary;

    List<RadialMenu.Item> items = new ArrayList<>();
    items.add(new RadialMenu.Item("attacker", 145));
    items.add(new RadialMenu.Item(SELECT_MENU_ID, 35));
    items.add(new RadialMenu.Item(CANCEL_MENU_ID, 35));
    items.add(new RadialMenu.Item("defender", 145));

    menu = new RadialMenu(engine, entityFactory, position, camera, items, 100f);
  }

  public void addSelectedHandler(Consumer<Action> handler) {
    menu.setClickHandler((action) -> {
      switch (action) {
        case CANCEL_MENU_ID:
          handler.accept(Action.REJECT);
          break;
        case SELECT_MENU_ID:
          handler.accept(Action.CONFIRM);
          break;
      }
    });
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {

    return menu.touchUp(screenX, screenY, pointer, button);
  }

  @Override
  public void render() {
    menu.render();

    renderResult(summary.getDefenderResult(), ResultSide.LEFT);
    renderResult(summary.getAttackerResult(), ResultSide.RIGHT);
    renderText("NO", getOuterRimPosition(190).add(0, 40), true);
    renderText("YES", getOuterRimPosition(170).add(0, 40), true);
  }

  @Override
  public void dispose() {
    super.dispose();
    menu.dispose();
  }

  /**
   * Render a combatant result
   * @param result the {@link CombatantBattleResult} battle result
   * @param side which side to render it on
   */
  private void renderResult(CombatantBattleResult result, ResultSide side) {
    int degreesMultiplier = side == ResultSide.LEFT ? 1 : -1;
    int hp = result.getCombatant().getHealth();
    renderStat(hp, "health", getOuterRimPosition(degreesMultiplier * 30), side);

    int damage = result.getDamage();
    renderStat(damage, "damage", getOuterRimPosition(degreesMultiplier * 50), side);

    int hitChance = result.getHitChancePct();
    renderStat(hitChance, "hit%", getOuterRimPosition(degreesMultiplier * 65), side);

    int critChance = result.getCritChancePct();
    renderStat(critChance, "crit%", getOuterRimPosition(degreesMultiplier * 80), side);
  }

  /**
   * Render a single stat.
   * @param value the number value of the stat
   * @param label the name of the stat
   * @param position its position
   */
  private void renderStat(int value, String label, Vector2 position, ResultSide side) {
    if (side == ResultSide.RIGHT) {
      Entity statNumber = entityFactory.createRightAlignedText(String.valueOf(value), position);
      newEntity(statNumber);

      Entity statLabel = entityFactory.createRightAlignedText(label, position.cpy().sub(0, 10));
      newEntity(statLabel);
    } else {
      renderText(String.valueOf(value), position, false);
      renderText(label, position.cpy().sub(0, 10), false);
    }
  }

  /**
   * Get a position along the outer ring where the text is aligned
   *
   * @param degrees degrees from up, rotated clockwise
   */
  private Vector2 getOuterRimPosition(float degrees) {
    Vector2 above = new Vector2(position.x, position.y + menu.getRadius());
    return MathUtils.rotateAroundPoint(position, above, degrees);
  }

  // Render text at a given position
  private void renderText(String text, Vector2 position, boolean centered) {
    if (centered) {
      Entity textEntity = entityFactory.createCenteredText(text, position);
      newEntity(textEntity);
    } else {
      Entity textEntity = entityFactory.createText(text, position, Vector2.Zero);
      newEntity(textEntity);

    }
  }
}
