package com.ziodyne.sometrpg.view.entities;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.CombatantAnimationSequence;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.tween.SpriteComponentAccessor;

import java.util.List;

public class RenderedCombatant implements Positioned {
  private final Combatant combatant;
  private final Position positionComponent;
  private final BattleUnit battleUnitComponent;
  private final DamageTintUpdater tintUpdater;
  private final SpriteComponent spriteComponent;
  private final Entity entity;

  public RenderedCombatant(Entity entity, Combatant combatant, Position positionComponent, BattleUnit battleUnitComponent,
                           SpriteComponent spriteComponent, DamageTintUpdater tintUpdater) {
    this.entity = entity;
    this.combatant = combatant;
    this.spriteComponent = spriteComponent;
    this.positionComponent = positionComponent;
    this.battleUnitComponent = battleUnitComponent;
    this.tintUpdater = tintUpdater;
  }

  public void fadeToDeath(TweenManager tweenManager, Engine engine) {
    // Get rid of the tint shader, which doesn't really account for alpha. Maybe fix this?

    entity.remove(Shader.class);
    Tween.to(spriteComponent, SpriteComponentAccessor.ALPHA, 0.5f)
            .target(0f)
            .ease(TweenEquations.easeOutCubic)
            .setCallback((type, source) -> {
              engine.removeEntity(entity);
            })
            .start(tweenManager);
  }

  public void setAnimationSequence(List<AnimationType> types) {

    entity.add(new CombatantAnimationSequence(battleUnitComponent, types));
  }

  public void setAnimation(AnimationType type) {
    battleUnitComponent.setAnimType(type);
  }

  public Animation getAnimation(AnimationType type) {
    return battleUnitComponent.getAnimation(type);
  }

  public void setTintAmount(float amount) {
    tintUpdater.setAmount(amount);
  }

  public float getTintAmount() {
    return tintUpdater.getAmount();
  }

  public void setAnimationType(AnimationType animationType) {
    battleUnitComponent.setAnimType(animationType);
  }

  public Combatant getCombatant() {
    return combatant;
  }

  @Override
  public void setPosition(Vector2 pos) {
    positionComponent.setX(pos.x);
    positionComponent.setY(pos.y);
  }

  @Override
  public Vector2 getPosition() {
    return new Vector2(positionComponent.getX(), positionComponent.getY());
  }
}
