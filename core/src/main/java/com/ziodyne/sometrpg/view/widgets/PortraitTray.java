package com.ziodyne.sometrpg.view.widgets;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.ziodyne.sometrpg.events.CloseTray;
import com.ziodyne.sometrpg.events.CombatantActed;
import com.ziodyne.sometrpg.events.OpenTray;
import com.ziodyne.sometrpg.events.ToggleTray;
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
import java.util.Map;
import java.util.Set;

public class PortraitTray extends Widget implements Logged {
  private final Collection<Combatant> combatants;
  private final EntityFactory entityFactory;
  private final Viewport viewport;
  private final Battle battle;
  private final TweenManager tweenManager;
  private final Map<Combatant, IconEntities> icons = new HashMap<>();
  private final Position rootPosition;
  private final Vector2 openPosition;
  private final Vector2 closedPosition;

  private boolean open = false;

  public PortraitTray(Engine engine, EntityFactory entityFactory, Battle battle, Viewport viewport, EventBus eventBus, TweenManager tweenManager) {
    super(engine);
    eventBus.register(this);

    this.combatants = battle.getPlayerUnits();
    this.entityFactory = entityFactory;
    this.battle = battle;
    this.viewport = viewport;
    this.tweenManager = tweenManager;
    this.rootPosition = new Position(viewport.getViewportWidth(), viewport.getViewportHeight() - 200);
    this.openPosition = new Vector2(rootPosition.getX() - 200, rootPosition.getY());
    this.closedPosition = new Vector2(rootPosition.getX(), rootPosition.getY());
  }

  @Subscribe
  public void openTray(OpenTray evt) {
    show();
  }

  @Subscribe
  public void closeTray(CloseTray evt) {
    hide();
  }

  @Subscribe
  public void toggleTray(ToggleTray evt) {
    if (open) {
      hide();
    } else {
      show();
    }
  }

  @Override
  public void render() {

    // For each portrait, draw them stacked, from top to bottom.
    float topOffset = 0;
    for (Combatant combatant : combatants) {
      Character character = combatant.getCharacter();
      IconEntities iconEntities = new IconEntities();
      Entity portrait = newEntity(entityFactory.createDockPortrait(character, rootPosition, new Vector2(0, -topOffset)));
      iconEntities.portrait = portrait;
      Set<CombatantAction> actions = battle.getAvailableActions(combatant);


      if (actions.contains(CombatantAction.MOVE)) {
        Entity moveIcon = newEntity(entityFactory.createPortraitMoveIcon(rootPosition, new Vector2(20, 20 - topOffset)));
        iconEntities.move = moveIcon;
      }

      if (actions.contains(CombatantAction.ATTACK)) {
        Entity attack = newEntity(entityFactory.createPortraitAttackIcon(rootPosition, new Vector2(0, 20 - topOffset)));
        iconEntities.attack = attack;
      }

      icons.put(combatant, iconEntities);

      topOffset += 100;
    }
  }

  private void hide() {
    if (!open) {
      return;
    }

    Tween.to(rootPosition, 0, 0.2f)
          .ease(TweenEquations.easeOutCubic)
          .target(closedPosition.x, closedPosition.y)
          .setCallback((type, source) -> {
            open = false;
          })
          .start(tweenManager);
  }

  private void show() {
    if (open) {
      return;
    }

    Tween.to(rootPosition, 0, 0.2f)
          .ease(TweenEquations.easeOutCubic)
          .target(openPosition.x, openPosition.y)
          .setCallback((type, source) -> {
            open = true;
          })
          .start(tweenManager);
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
        entities.move = newEntity(entityFactory.createPortraitMoveIcon(rootPosition, new Vector2(pos.getX(), pos.getY() - 20)));
      }
    }

    if (!actions.contains(CombatantAction.ATTACK)) {
      removeEntity(entities.attack);
      entities.attack = null;
    } else {
      if (entities.attack == null) {
        entities.attack = newEntity(entityFactory.createPortraitAttackIcon(rootPosition, new Vector2(pos.getX(), pos.getY() - 20)));
      }
    }
  }

  private static class IconEntities {
    public Entity attack;
    public Entity move;
    public Entity portrait;
  }
}
