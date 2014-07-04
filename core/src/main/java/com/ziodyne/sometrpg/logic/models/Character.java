package com.ziodyne.sometrpg.logic.models;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import org.apache.commons.lang3.StringUtils;

import com.ziodyne.sometrpg.logic.util.UnitUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * This is the representation of a character that is not related to a specific unit on the battlefield.
 */
public class Character {
  private static final AtomicLong lastIdentifier = new AtomicLong(0L);
  
  private final long id;
  private final Map<Stat, Integer> maxStatSheet;
  private final UnitGrowth growths;
  private String name;
  private Map<Stat, Integer> statSheet;
  private Set<CombatantAction> attackActions = EnumSet.of(CombatantAction.ATTACK);

  public Character(Map<Stat, Integer> maxStatSheet, UnitGrowth growths, String name, Map<Stat, Integer> statSheet,
                   Set<CombatantAction> attackActions) {
    this(maxStatSheet, growths, statSheet, name);
    this.attackActions = Sets.union(this.attackActions, attackActions);
  }

  public Character(Map<Stat, Integer> maxStatSheet, UnitGrowth growths, Map<Stat, Integer> statSheet, String name) {
    this.id = lastIdentifier.incrementAndGet();
    this.maxStatSheet = Objects.requireNonNull(maxStatSheet);
    this.growths = Objects.requireNonNull(growths);
    this.statSheet = Objects.requireNonNull(statSheet);    
    this.name = Objects.requireNonNull(StringUtils.trimToNull(name));
    
    validateStats();
  }

  public String getArmyName() {
    return null;
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

  public UnitGrowth getGrowths() {
    return growths;
  }

  public long getId() {
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
