package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.logic.models.*;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import org.apache.commons.lang3.StringUtils;

/**
 * This widget renders the menu for a unit's stats and in-combat information.
 */
public class UnitInfoMenu extends Widget {
  private static final float INNER_GUTTER_PX = 25f;
  private static final float OUTER_GUTTER_PX = 40f;

  private final EntityFactory entityFactory;
  private final Combatant combatant;
  private final Viewport viewport;

  public UnitInfoMenu(Engine engine, EntityFactory entityFactory, Viewport viewport, Combatant combatant) {

    super(engine);
    this.entityFactory = entityFactory;
    this.combatant = combatant;
    this.viewport = viewport;
  }

  @Override
  public void render() {

    float height = viewport.getViewportHeight() * .75f;

    float rightWidth = viewport.getViewportWidth() * .8f;
    float leftWidth = viewport.getViewportWidth() * .2f;

    Vector2 leftPanePos = new Vector2(OUTER_GUTTER_PX, OUTER_GUTTER_PX);
    renderPane(leftPanePos, leftWidth, height);

    Vector2 rightPanePos = new Vector2(OUTER_GUTTER_PX + leftWidth + INNER_GUTTER_PX, OUTER_GUTTER_PX);
    renderPane(rightPanePos, rightWidth, height);

    Character character = combatant.getCharacter();
    String characterName = StringUtils.upperCase(character.getName());
    Vector2 charNamePos = new Vector2(OUTER_GUTTER_PX + INNER_GUTTER_PX, OUTER_GUTTER_PX + height);
    renderText(characterName, charNamePos);

    int level = character.getStat(Stat.LEVEL);
    String levelLabel = "LEVEL " + level;
    Vector2 levelPosition = new Vector2(OUTER_GUTTER_PX + INNER_GUTTER_PX, OUTER_GUTTER_PX + height - 100);
    renderText(levelLabel, levelPosition);

    String hpLabel = "HP";
    Vector2 hpLabelPosition = new Vector2(OUTER_GUTTER_PX + INNER_GUTTER_PX, OUTER_GUTTER_PX + height - 200);
    renderText(hpLabel, hpLabelPosition);

    int health = combatant.getHealth();
    int maxHealth = character.getStat(Stat.HP);
    Vector2 healthPosition = new Vector2(OUTER_GUTTER_PX + INNER_GUTTER_PX + leftWidth - 220, OUTER_GUTTER_PX + height - 200);
    renderText(health + "/" + maxHealth, healthPosition);

    String movLabel = "MOV";
    Vector2 moveLabelPos = new Vector2(OUTER_GUTTER_PX + INNER_GUTTER_PX, OUTER_GUTTER_PX + height - 300);
    renderText(movLabel, moveLabelPos);

    String movementRange = String.valueOf(combatant.getMovementRange());
    Vector2 movNumberPos = new Vector2(OUTER_GUTTER_PX + INNER_GUTTER_PX + leftWidth - 110, OUTER_GUTTER_PX + height - 300);
    renderText(movementRange, movNumberPos);
  }

  private void renderText(String text, Vector2 position) {

    Entity label = entityFactory.createViewportText(text, position);
    newEntity(label);
  }

  private void renderPane(Vector2 position, float width, float height) {

    Entity pane = entityFactory.createMenuBg(position, width, height);
    newEntity(pane);
  }
}
