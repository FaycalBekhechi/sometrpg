package com.ziodyne.sometrpg.logic.models;
/**
 * A single statistic on a unit.
 * @author Ryan
 *
 */
public class UnitStat {
  private final int value;
  private final Stat stat;
  
  public UnitStat(int value, Stat stat) {
    super();
    if (value > Constants.STAT_MAX) {
      throw new IllegalArgumentException("Stat too high! " + value + " provided, max is: " + Constants.STAT_MAX);
    }
    
    this.value = value;
    this.stat = stat;
  }
  public int getValue() {
    return value;
  }
  public Stat getStat() {
    return stat;
  }
}
