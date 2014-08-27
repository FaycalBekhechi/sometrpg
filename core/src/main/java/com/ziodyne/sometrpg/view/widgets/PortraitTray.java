package com.ziodyne.sometrpg.view.widgets;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.EventBus;
import com.ziodyne.sometrpg.events.CombatantActed;
import com.ziodyne.sometrpg.events.EventListener;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

import java.util.Collection;
import java.util.Set;

public class PortraitTray extends Widget {
  private final Collection<Combatant> combatants;
  private final EntityFactory entityFactory;
  private final Viewport viewport;
  private final Battle battle;

  public PortraitTray(Engine engine, EntityFactory entityFactory, Battle battle, Viewport viewport, EventBus eventBus) {
    super(engine);
    eventBus.register((EventListener<CombatantActed>) combatantActed -> updateIcons());

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
      newEntity(entityFactory.createDockPortrait(character, position));
      Set<CombatantAction> actions = battle.getAvailableActions(combatant);

      if (actions.contains(CombatantAction.MOVE)) {
        newEntity(entityFactory.createPortraitAttackIcon(position.cpy().sub(-20, 20)));
      }

      if (actions.contains(CombatantAction.ATTACK)) {
        newEntity(entityFactory.createPortraitMoveIcon(position.cpy().sub(0, 20)));
      }

      position = position.sub(0, 100);
    }
  }

  private void updateIcons() {

  }
}
