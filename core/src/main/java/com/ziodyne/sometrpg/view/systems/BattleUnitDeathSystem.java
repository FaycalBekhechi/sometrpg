package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.ComponentType;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.utils.Bits;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.DeathFade;
import com.ziodyne.sometrpg.view.components.SpriteComponent;

/**
 * Finds dead units and enqueues them for fade-out -> deletion.
 */
public class BattleUnitDeathSystem extends IteratingSystem {

  @SuppressWarnings("unchecked")
  public BattleUnitDeathSystem() {
    super(Family.getFamilyFor(ComponentType.getBitsFor(BattleUnit.class, SpriteComponent.class), new Bits(), ComponentType.getBitsFor(DeathFade.class)));
  }


  @Override
  public void processEntity(Entity entity, float deltaTime) {

    BattleUnit unitComponent = entity.getComponent(BattleUnit.class);
    Combatant combatant = unitComponent.combatant;
    if (!combatant.isAlive()) {
      entity.add(new DeathFade());
    }
  }
}
