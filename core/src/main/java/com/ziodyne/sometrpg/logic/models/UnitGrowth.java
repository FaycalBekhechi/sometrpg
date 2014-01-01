package com.ziodyne.sometrpg.logic.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UnitGrowth {
  @JsonProperty("rates")
  private Map<Stat, Float> growthRates = new HashMap<Stat, Float>();
  
  public UnitGrowth(Map<Stat, Float> rates) {
    this.growthRates = Objects.requireNonNull(rates);
  }
  
  public float getGrowthChance(UnitStat unitStat) {
    Stat stat = unitStat.getStat();
    
    return getGrowthChance(stat);
  }

  public float getGrowthChance(Stat stat) {
    // Units effectively have a 100% level growth rate no matter what.
    if (stat == Stat.LEVEL) { return 100f; }

    Float chance = growthRates.get(stat);
    if (chance == null) {
      throw new IllegalArgumentException(String.format("No stat growth defined for stat: \"%s\"", stat.name()));
    }

    return chance;
  }
}
