package com.ziodyne.sometrpg.logic.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ziodyne.sometrpg.logic.models.Stat;
import com.ziodyne.sometrpg.logic.models.Unit;
import com.ziodyne.sometrpg.logic.models.UnitGrowth;
import com.ziodyne.sometrpg.logic.models.UnitStat;

public class UnitUtils {
  private UnitUtils() { }
  
  public static void levelUp(Unit unit) {
    UnitGrowth growths = unit.getGrowths();
    
    Set<UnitStat> newStatSheet = new HashSet<UnitStat>(Stat.values().length);
    for (UnitStat stat : unit.getStatSheet()) {
      if (willGrow(growths, stat)) {
        newStatSheet.add(new UnitStat(stat.getValue() + 1, stat.getStat()));
      } else {
        newStatSheet.add(new UnitStat(stat.getValue(), stat.getStat()));
      }
    }
    
    unit.setStatSheet(newStatSheet);
  }
  
  private static boolean willGrow(UnitGrowth growths, UnitStat stat) {
    return Math.random() < growths.getGrowthChance(stat);
  }
  
  /**
   * Get an index of a {@link Unit}'s stat values by stat type.
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
}
