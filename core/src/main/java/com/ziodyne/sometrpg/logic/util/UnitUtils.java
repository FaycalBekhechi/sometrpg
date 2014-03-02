package com.ziodyne.sometrpg.logic.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;
import com.ziodyne.sometrpg.logic.models.UnitStat;

public class UnitUtils {
  private UnitUtils() { }

  public static final int DEFAULT_MOVEMENT_RANGE = 5;

  public static void levelUp(Character character) {
    UnitGrowth growths = character.getGrowths();
    
    Map<Stat, Integer> newStatSheet = new HashMap<Stat, Integer>(Stat.values().length);
    for (Map.Entry<Stat, Integer> stat : character.getStatSheet().entrySet()) {
      if (willGrow(growths, stat.getKey(), stat.getValue())) {
        newStatSheet.put(stat.getKey(), stat.getValue()+1);
      } else {
        newStatSheet.put(stat.getKey(), stat.getValue());
      }
    }
    
    character.setStatSheet(newStatSheet);
  }
  
  private static boolean willGrow(UnitGrowth growths, Stat stat, Integer value) {
    return Math.random() < growths.getGrowthChance(stat);
  }
  
  /**
   * Get an index of a {@link com.ziodyne.sometrpg.logic.models.Character}'s stat values by stat type.
   *
   * @param stats A {@link Collection} of {@link UnitStat} to index
   * @return A {@link Map} of {@link Stat} to {@link Integer} representing the unit's value in the given stat.
   */
  public static Map<Stat, Integer> indexStatSheetByValue(Collection<UnitStat> stats) {
    Map<Stat, Integer> index = new HashMap<Stat, Integer>();

    for (UnitStat stat : stats) {
      index.put(stat.getStat(), stat.getValue());
    }

    return index;
  }

  public static int getMaxHealth(Character character) {
    Map<Stat, Integer> stats = character.getStatSheet();
    Integer hp = stats.get(Stat.HP);
    if (hp == null) {
      throw new IllegalArgumentException("Unit has no health defined.");
    }

    return hp;
  }
}
