package com.ziodyne.sometrpg.logic.models;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;
import org.junit.Test;

import com.google.common.collect.Sets;

public class CharacterTest {
  private static Map<Stat, Integer> defaultStats() {
    StatSheetBuilder builder = new StatSheetBuilder();
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      builder.set(stat, 20);
    }

    return builder.build();
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStatsOverCap() {
    new Character(ModelTestUtils.homogeneousStats(20), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(40), "Test");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testStatsOverHardCap() {
    new Character(ModelTestUtils.homogeneousStats(200), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(400), "Test");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMaxStatsMissing() {
    Map<Stat, Integer> maxStats = defaultStats();
    maxStats.put(Stat.HP, 20);

    new Character(maxStats, ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(30), "test");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testStatsMissing() {
    Map<Stat, Integer> stats = defaultStats();
    stats.remove(Stat.HP);

    
    new Character(ModelTestUtils.homogeneousStats(30), ModelTestUtils.createGrowth(), stats, "test");
  }
}
