package com.ziodyne.sometrpg.logic.models.battle.combat;

import java.util.Random;

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
  private long id;

  public Combatant(Character character) {
    this.id = (long)(new Random().nextDouble() * Long.MAX_VALUE);
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

  /**
   * Get the percentage of health this combatant has left
   * @return the percent health remaining on the combatant from 0 to 1.
   */
  public float getHealthPct() {
    return health / (float)UnitUtils.getMaxHealth(character);
  }

  public long getId() {
    return id;
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
            .append(id, ((Combatant) obj).getId())
            .build();
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(character)
            .append(id)
            .build();
  }
}
