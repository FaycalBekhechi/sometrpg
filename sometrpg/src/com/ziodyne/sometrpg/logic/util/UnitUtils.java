package com.ziodyne.sometrpg.logic.util;

import java.util.HashSet;
import java.util.Set;

import com.ziodyne.sometrpg.models.Stat;
import com.ziodyne.sometrpg.models.Unit;
import com.ziodyne.sometrpg.models.UnitGrowth;
import com.ziodyne.sometrpg.models.UnitStat;

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
}
