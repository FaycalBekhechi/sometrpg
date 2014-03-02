package com.ziodyne.sometrpg.logic.models;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;

import com.google.common.base.Preconditions;

public class StatSheetBuilder {
  public Map<Stat, Integer> sheet = new EnumMap<Stat, Integer>(Stat.class);

  public StatSheetBuilder set(Stat stat, Integer value) {
    sheet.put(stat, value);
    return this;
  }

  public Map<Stat, Integer> build() {

    EnumSet<Stat> stats = EnumSet.allOf(Stat.class);
    for (Stat stat : stats) {
      Preconditions.checkState(sheet.containsKey(stat), "Stat sheet incomplete. Must declare a value for: " + stat);
    }

    return sheet;
  }
}
