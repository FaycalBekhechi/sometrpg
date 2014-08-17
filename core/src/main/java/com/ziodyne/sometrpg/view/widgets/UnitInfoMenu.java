package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import org.apache.commons.lang3.StringUtils;

public class UnitInfoMenu extends Widget {
  private EntityFactory entityFactory;
  private Combatant combatant;

  public UnitInfoMenu(Engine engine, EntityFactory entityFactory, Combatant combatant) {

    super(engine);
    this.entityFactory = entityFactory;
    this.combatant = combatant;
  }

  @Override
  public void render() {

    float outerGutter = 40;
    float innerGutter = 25;

    float leftWidth = 446.5f;
    Entity smallLeft = entityFactory.createMenuBg(new Vector2(outerGutter, outerGutter), leftWidth, 630);
    Entity largeRight = entityFactory.createMenuBg(new Vector2(outerGutter + leftWidth + innerGutter, outerGutter), 1046.5f, 630);
    newEntity(smallLeft);
    newEntity(largeRight);

    String characterName = combatant.getCharacter().getName();
    characterName = StringUtils.upperCase(characterName);

    Entity label = entityFactory.createViewportText(characterName, new Vector2(outerGutter + 20, outerGutter+610));
    newEntity(label);
  }
}
