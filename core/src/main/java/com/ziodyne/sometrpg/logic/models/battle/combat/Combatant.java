package com.ziodyne.sometrpg.logic.models.battle.combat;

import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.logic.util.UnitUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This is the representation of a character on the battlefield.
 */
public class Combatant {
  private Army army;
  private final Character character;
  private int health;

  public Combatant(Character character) {
    this.character = character;
    this.health = UnitUtils.getMaxHealth(character);
  }

  public Army getArmy() {
    return army;
  }

  public void setArmy(Army army) {
    this.army = army;
  }

  public void applyDamage(int amount) {
    // Overkill is the best kill, but keep things simple by bottoming out at 0.
    health = Math.max(health - amount, 0);
  }

  public long getUnitId() {
    return character.getId();
  }

  public boolean isAlive() {
    return health > 0;
  }

  public Character getCharacter() {
    return character;
  }

  public int getHealth() {
    return health;
  }

  public int getMovementRange() {
    return character.getMovementRange();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Combatant)) {
      return false;
    }

    return new EqualsBuilder()
            .append(character, ((Combatant) obj).getCharacter())
            .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(character)
            .build();
  }
}
