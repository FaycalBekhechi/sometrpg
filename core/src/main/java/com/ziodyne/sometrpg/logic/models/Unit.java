package com.ziodyne.sometrpg.logic.models;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;

import com.ziodyne.sometrpg.logic.util.UnitUtils;

public class Unit {
  private static final AtomicLong lastIdentifier = new AtomicLong(0L);
  
  private final long id;
  private String name;
  private Set<UnitStat> statSheet;
  private final Set<UnitStat> maxStatSheet;
  private final UnitGrowth growths;

  public Unit(Set<UnitStat> maxStatSheet, UnitGrowth growths, Set<UnitStat> statSheet, String name) {
    this.id = lastIdentifier.incrementAndGet();
    this.maxStatSheet = Objects.requireNonNull(maxStatSheet);
    this.growths = Objects.requireNonNull(growths);
    this.statSheet = Objects.requireNonNull(statSheet);    
    this.name = Objects.requireNonNull(StringUtils.trimToNull(name));
    
    validateStats();
  }

  private void validateStats() {
    Map<Stat, Integer> currentStats = UnitUtils.indexStatSheetByValue(statSheet);
    Map<Stat, Integer> maxStats = UnitUtils.indexStatSheetByValue(maxStatSheet);
    
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      Integer value = currentStats.get(stat);
      Integer cap = maxStats.get(stat);
      
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
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<UnitStat> getMaxStatSheet() {
    return maxStatSheet;
  }

  public Set<UnitStat> getStatSheet() {
    return statSheet;
  }

  public void setStatSheet(Set<UnitStat> statSheet) {
    this.statSheet = statSheet;
  }
  
  public UnitGrowth getGrowths() {
    return growths;
  }

  public long getId() {
    return id;
  }
}
