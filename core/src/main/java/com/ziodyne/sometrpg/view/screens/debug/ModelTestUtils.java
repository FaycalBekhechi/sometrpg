package com.ziodyne.sometrpg.view.screens.debug;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.models.Constants;
import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Character;
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

  public static Set<UnitStat> randomStats() {
    EnumSet<Stat> stats = EnumSet.allOf(Stat.class);

    Set<UnitStat> results = Sets.newHashSet();

    for (Stat stat : stats) {
      results.add(new UnitStat(Math.round((long) (Math.random() * Constants.STAT_MAX)), stat));
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
  
  public static Character createUnit() {
    return new Character(createMaxStats(), createGrowth(), createStats(), "Test");
  }

  public static Character createRandomUnit() {
    return new Character(homogeneousStats(Constants.STAT_MAX), createGrowth(), randomStats(), "Test Random");
  }
  
  public static Character createMaxedUnit() {
    return new Character(homogeneousStats(Constants.STAT_MAX), createGrowth(), homogeneousStats(Constants.STAT_MAX), "Test");
  }
  
  public static Character createCrappyUnit() {
    return new Character(createMaxStats(), createGrowth(), createMinStats(), "Test");
  }
}
