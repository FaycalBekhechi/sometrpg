package com.ziodyne.sometrpg.logic.models;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Sets;

public class UnitTest {

  @Test(expected = IllegalArgumentException.class)
  public void testStatsOverCap() {
    new Unit(ModelTestUtils.homogeneousStats(20), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(40), "Test");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testStatsOverHardCap() {
    new Unit(ModelTestUtils.homogeneousStats(2000), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(400), "Test");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testMaxStatsMissing() {
    Set<UnitStat> maxStats = Sets.newHashSet();
    maxStats.add(new UnitStat(20, Stat.HP));
    
    new Unit(maxStats, ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(30), "test");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testStatsMissing() {
    Set<UnitStat> stats = Sets.newHashSet();
    stats.add(new UnitStat(20, Stat.HP));
    
    new Unit(ModelTestUtils.homogeneousStats(30), ModelTestUtils.createGrowth(), stats, "test");
  }
}
