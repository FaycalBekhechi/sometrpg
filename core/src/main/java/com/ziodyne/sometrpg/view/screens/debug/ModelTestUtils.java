package com.ziodyne.sometrpg.view.screens.debug;

import java.util.EnumSet;
import java.util.Map;

import com.google.common.collect.Maps;
import com.ziodyne.sometrpg.logic.models.CharacterGrowth;
import com.ziodyne.sometrpg.logic.models.Constants;
import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.StatSheetBuilder;

public class ModelTestUtils {
  private ModelTestUtils() { }
  
  public static Map<Stat, Integer> homogeneousStats(int value) {
    StatSheetBuilder builder = new StatSheetBuilder();
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      builder.set(stat, value);
    }

    return builder.build();
  }

  public static Map<Stat, Integer> randomStats() {
    StatSheetBuilder builder = new StatSheetBuilder();
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      builder.set(stat, Math.round((long) (Math.random() * Constants.STAT_MAX)));
    }

    return builder.build();
  }
  
  public static Map<Stat, Integer> createStats() {
    return homogeneousStats(10);
  }
  
  public static Map<Stat, Integer> createMinStats() {
    return homogeneousStats(0);
  }
  
  public static Map<Stat, Integer> createMaxStats() {
    return homogeneousStats(40);
  }
  
  public static CharacterGrowth createGrowth() {
    Map<Stat, Float> rawGrowths = Maps.newHashMap();
    
    for (Stat stat : EnumSet.allOf(Stat.class)) {
      rawGrowths.put(stat, 40f);
    }
    
    return new CharacterGrowth(rawGrowths);
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
