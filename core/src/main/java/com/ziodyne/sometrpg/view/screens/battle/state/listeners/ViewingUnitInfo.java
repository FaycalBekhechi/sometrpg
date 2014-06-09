package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.Set;

import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;

public class ViewingUnitInfo extends FlowListener<BattleContext> {
  private final World world;
  private final EntityFactory entityFactory;
  private final Set<Entity> ownedEntities = Sets.newHashSet();

  public ViewingUnitInfo(World world, EntityFactory entityFactory) {

    super(BattleState.SHOWING_UNIT_DETAILS);
    this.world = world;
    this.entityFactory = entityFactory;
  }

  @Override
  public void onLeave(BattleContext context) {
    for (Entity entity : ownedEntities) {
      world.deleteEntity(entity);
    }
  }

  @Override
  public void onEnter(BattleContext context) {
    float outerGutter = 40;
    float innerGutter = 25;

    float leftWidth = 446.5f;

    Entity smallLeft = entityFactory.createMenuBg(new Vector2(outerGutter, outerGutter), leftWidth, 630);
    Entity largeRight = entityFactory.createMenuBg(new Vector2(outerGutter + leftWidth + innerGutter, outerGutter), 1046.5f, 630);

    world.addEntity(smallLeft);
    world.addEntity(largeRight);

    ownedEntities.add(smallLeft);
    ownedEntities.add(largeRight);
  }
}
