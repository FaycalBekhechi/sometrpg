package com.ziodyne.sometrpg.view.widgets;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ziodyne.sometrpg.events.CloseTray;
import com.ziodyne.sometrpg.events.CombatantActed;
import com.ziodyne.sometrpg.events.OpenTray;
import com.ziodyne.sometrpg.events.TurnStarted;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.util.Logged;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.entities.EntityFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

public class PortraitTray extends Widget implements Logged {
  private final Collection<Combatant> combatants;
  private final EntityFactory entityFactory;
  private final Viewport viewport;
  private final Battle battle;
  private final TweenManager tweenManager;
  private final Map<Combatant, IconEntities> icons = new HashMap<>();
  private boolean open = false;

  public PortraitTray(Engine engine, EntityFactory entityFactory, Battle battle, Viewport viewport, EventBus eventBus, TweenManager tweenManager) {
    super(engine);
    eventBus.register(this);

    this.combatants = battle.getPlayerUnits();
    this.entityFactory = entityFactory;
    this.battle = battle;
    this.viewport = viewport;
    this.tweenManager = tweenManager;
  }

  @Subscribe
  public void openTray(OpenTray evt) {
    show();
  }

  @Subscribe
  public void closeTray(CloseTray evt) {
    hide();
  }

  @Override
  public void render() {
    Vector2 position = new Vector2(viewport.getViewportWidth(), viewport.getViewportHeight() - 200);
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

  private void hide() {
    if (!open) {
      return;
    }

    forAllPositions((pos) -> {
      Tween.to(pos, 0, 0.3f)
              .ease(TweenEquations.easeOutCubic)
              .target(pos.getX() + 200, pos.getY())
              .start(tweenManager);
    });
    open = false;
  }

  private void show() {
    if (open) {
      return;
    }

    forAllPositions((pos) -> {
      Tween.to(pos, 0, 0.3f)
              .ease(TweenEquations.easeOutCubic)
              .target(pos.getX() - 200, pos.getY())
              .start(tweenManager);
    });
    open = true;
  }

  private void forAllPositions(Consumer<Position> action) {
    for (IconEntities entities : icons.values()) {
      List<Position> positions = Lists.newArrayList(
              entities.attack.getComponent(Position.class),
              entities.portrait.getComponent(Position.class),
              entities.move.getComponent(Position.class)
      );

      for (Position pos : positions) {
        action.accept(pos);
      }
    }
  }

  @Subscribe
  public void updateIcons(CombatantActed actedEvent) {
    Combatant combatant = actedEvent.getCombatant();
    diffItems(combatant);
  }

  @Subscribe
  public void updateAllIcons(TurnStarted turnStarted) {
    for (Combatant combatant : combatants) {
      diffItems(combatant);
    }
  }

  private void diffItems(Combatant combatant) {

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
      if (entities.attack == null) {
        entities.attack = newEntity(entityFactory.createPortraitAttackIcon(new Vector2(pos.getX(), pos.getY() - 20)));
      }
    }
  }

  private static class IconEntities {
    public Entity attack;
    public Entity move;
    public Entity portrait;
  }
}
