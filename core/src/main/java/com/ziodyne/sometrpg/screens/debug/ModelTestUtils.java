package com.ziodyne.sometrpg.screens.debug;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.Constants;
import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;
import com.ziodyne.sometrpg.logic.models.UnitStat;

public class ModelTestUtils {
  private ModelTestUtils() { }
  
  public static Set<UnitStat> homogeneousStats(int value) {
    EnumSet<Stat> stats = EnumSet.allOf(Stat.class);
    
    Set<UnitStat> results = Sets.newHashSet();
    
    for (Stat stat : stats) {
      results.add(new UnitStat(value, stat));
    }
    
    return results;  
  }
  
  public static Set<UnitStat> createStats() {
    return homogeneousStats(10);
  }
  
  public static Set<UnitStat> createMinStats() {
    return homogeneousStats(0);
  }
  
  public static Set<UnitStat> createMaxStats() {
    return homogeneousStats(40);
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
  
  public static Unit createMaxedUnit() {
    return new Unit(homogeneousStats(Constants.STAT_MAX), createGrowth(), homogeneousStats(Constants.STAT_MAX), "Test");
  }
  
  public static Unit createCrappyUnit() {
    return new Unit(createMaxStats(), createGrowth(), createMinStats(), "Test");
  }
}
