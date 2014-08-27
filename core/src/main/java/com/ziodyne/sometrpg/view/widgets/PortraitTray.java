package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.EventBus;
import com.ziodyne.sometrpg.events.CombatantActed;
import com.ziodyne.sometrpg.events.EventListener;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PortraitTray extends Widget implements Logged {
  private final Collection<Combatant> combatants;
  private final EntityFactory entityFactory;
  private final Viewport viewport;
  private final Battle battle;
  private final Map<Combatant, IconEntities> icons = new HashMap<>();

  private static class IconEntities {
    public Entity attack;
    public Entity move;
    public Entity portrait;
  }

  public PortraitTray(Engine engine, EntityFactory entityFactory, Battle battle, Viewport viewport, EventBus eventBus) {
    super(engine);
    eventBus.register((EventListener<CombatantActed>) this::updateIcons);

    this.combatants = battle.getPlayerUnits();
    this.entityFactory = entityFactory;
    this.battle = battle;
    this.viewport = viewport;
  }

  @Override
  public void render() {
    Vector2 position = new Vector2(viewport.getViewportWidth() - 200, viewport.getViewportHeight() - 200);
    for (Combatant combatant : combatants) {
      Character character = combatant.getCharacter();
      IconEntities iconEntities = new IconEntities();
      Entity portrait = newEntity(entityFactory.createDockPortrait(character, position));
      iconEntities.portrait = portrait;
      Set<CombatantAction> actions = battle.getAvailableActions(combatant);


      if (actions.contains(CombatantAction.MOVE)) {
        Entity moveIcon = newEntity(entityFactory.createPortraitMoveIcon(position.cpy().sub(-20, 20)));
        iconEntities.move = moveIcon;
      }

      if (actions.contains(CombatantAction.ATTACK)) {
        Entity attack = newEntity(entityFactory.createPortraitAttackIcon(position.cpy().sub(0, 20)));
        iconEntities.attack = attack;
      }

      icons.put(combatant, iconEntities);

      position = position.sub(0, 100);
    }
  }

  private void updateIcons(CombatantActed actedEvent) {
    Combatant combatant = actedEvent.getCombatant();
    IconEntities entities = icons.get(combatant);
    Position pos = entities.portrait.getComponent(Position.class);

    Set<CombatantAction> actions = battle.getAvailableActions(combatant);

    // TODO: Refactor this janky diff update
    if (!actions.contains(CombatantAction.MOVE)) {
      removeEntity(entities.move);
      entities.move = null;
    } else {
      if (entities.move == null) {
        entities.move = newEntity(entityFactory.createPortraitMoveIcon(new Vector2(pos.getX(), pos.getY() - 20)));
      }
    }

    if (!actions.contains(CombatantAction.ATTACK)) {
      removeEntity(entities.attack);
      entities.attack = null;
    } else {
      if (entities.attack == null)  {
        entities.attack = newEntity(entityFactory.createPortraitAttackIcon(new Vector2(pos.getX(), pos.getY() - 20)));
      }
    }
  }
}
