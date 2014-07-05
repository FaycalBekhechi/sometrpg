package com.ziodyne.sometrpg.logic.models;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import org.apache.commons.lang3.StringUtils;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This is the representation of a character that is not related to a specific unit on the battlefield.
 */
public class Character {

  private final String id;
  private final Map<Stat, Integer> maxStatSheet;
  private final CharacterGrowth growths;
  private String name;
  private String armyName;
  private Map<Stat, Integer> statSheet;
  private Set<CombatantAction> attackActions = EnumSet.of(CombatantAction.ATTACK);

  public Character(Map<Stat, Integer> maxStatSheet, CharacterGrowth growths, String name, Map<Stat, Integer> statSheet,
                   Set<CombatantAction> attackActions) {
    this(maxStatSheet, growths, statSheet, name);
    this.attackActions = Sets.union(this.attackActions, attackActions);
  }

  public Character(String id, Map<Stat, Integer> maxStatSheet, CharacterGrowth growths, Map<Stat, Integer> statSheet, String name, String armyName) {

    this.id = Objects.requireNonNull(StringUtils.trimToNull(id));
    this.maxStatSheet = Objects.requireNonNull(maxStatSheet);
    this.growths = Objects.requireNonNull(growths);
    this.statSheet = Objects.requireNonNull(statSheet);
    this.name = Objects.requireNonNull(StringUtils.trimToNull(name));
    this.armyName = Objects.requireNonNull(StringUtils.trimToNull(armyName));

    validateStats();
  }

  public Character(Map<Stat, Integer> maxStatSheet, CharacterGrowth growths, Map<Stat, Integer> statSheet, String name) {
    this.id = "";
    this.maxStatSheet = Objects.requireNonNull(maxStatSheet);
    this.growths = Objects.requireNonNull(growths);
    this.statSheet = Objects.requireNonNull(statSheet);    
    this.name = Objects.requireNonNull(StringUtils.trimToNull(name));
    
    validateStats();
  }

  public String getArmyName() {
    return armyName;
  }

  public int getMovementRange() {
    return statSheet.get(Stat.MOVEMENT);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Map<Stat, Integer> getMaxStatSheet() {

    return maxStatSheet;
  }

  public Map<Stat, Integer> getStatSheet() {

    return statSheet;
  }

  public void setStatSheet(Map<Stat, Integer> statSheet) {

    this.statSheet = statSheet;
  }

  public void setAttackActions(Set<CombatantAction> attackActions) {

    this.attackActions = attackActions;
  }

  public CharacterGrowth getGrowths() {
    return growths;
  }

  public String getId() {
    return id;
  }

  public Set<CombatantAction> getAttackActions() {
    return attackActions;
  }

  private void validateStats() {
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      Integer value = statSheet.get(stat);
      Integer cap = maxStatSheet.get(stat);

      if (value == null) {
        throw new IllegalArgumentException("Unit missing stat value for: " + stat.name());
      }

      if (cap == null) {
        throw new IllegalArgumentException("Unit missing stat cap value for: " + stat.name());
      }

      if (value > cap) {
        throw new IllegalArgumentException("Unit stats too high for: " + stat.name());
      }
    }
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(17, 37)
            .append(id)
            .build();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null) {
      return false;
    }

    if (!(obj instanceof Character)) {
      return false;
    }

    return new EqualsBuilder()
            .append(id, ((Character) obj).getId())
            .build();
  }
}
