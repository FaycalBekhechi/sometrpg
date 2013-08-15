package com.ziodyne.sometrpg.models;
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
