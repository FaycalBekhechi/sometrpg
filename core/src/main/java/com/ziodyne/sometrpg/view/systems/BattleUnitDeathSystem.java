package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.components.BattleUnit;

/**
 * Removes all dead combatant entities from the world.
 */
public class BattleUnitDeathSystem extends EntityProcessingSystem {
  @Mapper
  private ComponentMapper<BattleUnit> unitComponentMapper;

  @SuppressWarnings("unchecked")
  public BattleUnitDeathSystem() {
    super(Aspect.getAspectForAll(BattleUnit.class));
  }

  @Override
  protected void process(Entity entity) {
    BattleUnit unitComponent = unitComponentMapper.get(entity);
    Combatant combatant = unitComponent.combatant;
    if (!combatant.isAlive()) {
      entity.deleteFromWorld();
    }
  }
}
