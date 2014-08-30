package com.ziodyne.sometrpg.view.entities;

import com.badlogic.gdx.math.Vector2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.components.BattleUnit;
import com.ziodyne.sometrpg.view.components.Position;

public class RenderedCombatant implements Positioned {
  private final Combatant combatant;
  private final Position positionComponent;
  private final BattleUnit battleUnitComponent;

  public RenderedCombatant(Combatant combatant, Position positionComponent, BattleUnit battleUnitComponent) {
    this.combatant = combatant;
    this.positionComponent = positionComponent;
    this.battleUnitComponent = battleUnitComponent;
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
