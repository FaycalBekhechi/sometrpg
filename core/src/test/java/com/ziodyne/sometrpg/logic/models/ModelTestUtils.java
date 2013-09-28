package com.ziodyne.sometrpg.logic.models;

import java.util.EnumSet;
import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class ModelTestUtils {
  private ModelTestUtils() { }
  
  public static Set<UnitStat> createStats() {
    EnumSet<Stat> stats = EnumSet.allOf(Stat.class);
    
    Set<UnitStat> results = Sets.newHashSet();
    
    for (Stat stat : stats) {
      results.add(new UnitStat(10, stat));
    }
    
    return results; 
  }
  
  public static Set<UnitStat> createMaxStats() {
    EnumSet<Stat> stats = EnumSet.allOf(Stat.class);
    
    Set<UnitStat> results = Sets.newHashSet();
    
    for (Stat stat : stats) {
      results.add(new UnitStat(20, stat));
    }
    
    return results;
  }
  
  public static UnitGrowth createGrowth() {
    Map<Stat, Float> rawGrowths = Maps.newHashMap();
    
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      rawGrowths.put(stat, 40f);
    }
    
    return new UnitGrowth(rawGrowths);
  }
  
  public static Unit createUnit() {
    return new Unit(createMaxStats(), createGrowth(), createStats(), "Test");
  }
}
