package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.DeathFade;
import com.ziodyne.sometrpg.view.components.Sprite;

/**
 * Finds dead units and enqueues them for fade-out -> deletion.
 */
public class BattleUnitDeathSystem extends EntityProcessingSystem {
  @Mapper
  private ComponentMapper<BattleUnit> unitComponentMapper;

  @SuppressWarnings("unchecked")
  public BattleUnitDeathSystem() {
    super(Aspect.getAspectForAll(BattleUnit.class, Sprite.class).exclude(DeathFade.class));
  }

  @Override
  protected void process(final Entity entity) {
    BattleUnit unitComponent = unitComponentMapper.get(entity);
    Combatant combatant = unitComponent.combatant;
    if (!combatant.isAlive()) {
      entity.addComponent(new DeathFade());
    }
  }
}
