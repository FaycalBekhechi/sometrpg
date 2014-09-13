package com.ziodyne.sometrpg.view.controllers;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.view.entities.EntityFactory;
import com.ziodyne.sometrpg.view.entities.HealthBar;
import com.ziodyne.sometrpg.view.entities.RenderedCombatant;
import com.ziodyne.sometrpg.view.widgets.ActionMenu;
import com.ziodyne.sometrpg.view.widgets.UnitInfoMenu;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class BattleMenuController implements MenuController {
  private Optional<Set<HealthBar>> activeHealthBars = Optional.empty();
  private final EntityFactory entityFactory;
  private final Engine engine;
  private final Viewport viewport;

  public BattleMenuController(EntityFactory entityFactory, Engine engine, Viewport viewport) {
    this.entityFactory = entityFactory;
    this.engine = engine;
    this.viewport = viewport;
  }

  @Override
  public boolean healthBarsEnabled() {

    return activeHealthBars.isPresent();
  }

  @Override
  public void enableHealthBars(Collection<RenderedCombatant> combatants) {

    Set<HealthBar> bars = combatants.stream()
            .map(entityFactory::createHealthBar)
            .collect(Collectors.toSet());

    activeHealthBars = Optional.of(bars);
  }

  @Override
  public void disableHealthBars() {

    activeHealthBars.get().stream()
            .forEach(HealthBar::dispose);

    activeHealthBars = Optional.empty();
  }

  @Override
  public UnitInfoMenu showUnitInfo(Combatant combatant) {

    UnitInfoMenu menu = new UnitInfoMenu(engine, entityFactory, viewport, combatant);
    menu.render();
    return menu;
  }

  @Override
  public ActionMenu showActionMenu(Set<CombatantAction> actions, Vector2 position) {

    return new ActionMenu(actions, position, viewport.getCamera(), engine, entityFactory);
  }
}
