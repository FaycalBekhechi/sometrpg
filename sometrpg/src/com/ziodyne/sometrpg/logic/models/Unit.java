package com.ziodyne.sometrpg.logic.models;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Unit {
  private static final AtomicLong lastIdentifier = new AtomicLong(0L);
  
  private final long id;
  private String name;
  private Set<UnitStat> statSheet;
  private final Set<UnitStat> maxStatSheet;
  private final UnitGrowth growths;

  public Unit(long id, Set<UnitStat> maxStatSheet, UnitGrowth growths, Set<UnitStat> statSheet, String name) {
    this.id = lastIdentifier.incrementAndGet();
    this.maxStatSheet = maxStatSheet;
    this.growths = growths;
    this.statSheet = statSheet;
    this.name = name;
  }

    public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Set<UnitStat> getStatSheet() {
    return statSheet;
  }

  public void setStatSheet(Set<UnitStat> statSheet) {
    this.statSheet = statSheet;
  }
  
  public UnitGrowth getGrowths() {
    return growths;
  }

  public long getId() {
    return id;
  }
}
