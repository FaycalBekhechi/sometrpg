package com.ziodyne.sometrpg.view.screens.battle.state.listeners;

import java.util.Set;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleContext;
import com.ziodyne.sometrpg.view.screens.battle.state.BattleState;
import com.ziodyne.sometrpg.view.screens.battle.state.FlowListener;
import com.ziodyne.sometrpg.view.widgets.UnitInfoMenu;

public class ViewingUnitInfo extends FlowListener<BattleContext> {
  private final Engine engine;
  private final EntityFactory entityFactory;
  private final Set<Entity> ownedEntities = Sets.newHashSet();
  private UnitInfoMenu infoMenu;

  public ViewingUnitInfo(Engine engine, EntityFactory entityFactory) {

    super(BattleState.SHOWING_UNIT_DETAILS);
    this.engine = engine;
    this.entityFactory = entityFactory;
  }

  @Override
  public void onLeave(BattleContext context) {
    for (Entity entity : ownedEntities) {
      engine.removeEntity(entity);
    }

    if (infoMenu != null) {
      infoMenu.dispose();
    }
  }

  @Override
  public void onEnter(BattleContext context) {
    infoMenu = new UnitInfoMenu(engine, entityFactory, context.selectedCombatant);
    infoMenu.render();
  }
}
