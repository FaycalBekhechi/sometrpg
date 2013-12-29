package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Mapper;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.Position;

public class BattleUnitMovementSystem extends EntityProcessingSystem {
  private final BattleMap map;

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<BattleUnit> unitMapper;

  public BattleUnitMovementSystem(BattleMap map) {
    super(Aspect.getAspectForAll(Position.class, BattleUnit.class));
    this.map = map;
  }

  @Override
  protected void process(Entity entity) {
    BattleUnit unitCompoent = unitMapper.get(entity);
    GridPoint2 currentPosition = map.getCombatantPosition(unitCompoent.combatant);

    if (currentPosition != null) {
      Position posComponent = positionMapper.get(entity);
      posComponent.setX(currentPosition.x);
      posComponent.setY(currentPosition.y);
    }
  }
}
