package com.ziodyne.sometrpg.models;

import java.util.HashMap;
import java.util.Map;

public class UnitGrowth {
  private Map<Stat, Float> growthRates = new HashMap<Stat, Float>();
  
  public float getGrowthChance(UnitStat unitStat) {
    Stat stat = unitStat.getStat();
    
    // Units effectively have a 100% level growth rate no matter what.
    if (stat == Stat.LEVEL) { return 100f; }
    
    Float chance = growthRates.get(stat);
    if (chance == null) {
      throw new IllegalArgumentException(String.format("No stat growth defined for stat: \"%s\"", stat.name()));
    }
    
    return chance;
  }
}
