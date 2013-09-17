package com.ziodyne.sometrpg.logic.models;

import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

public class Unit {
  private static final AtomicLong lastIdentifier = new AtomicLong(0L);
  
  private final long id;
  private String name;
  private Set<UnitStat> statSheet;
  private final UnitGrowth growths; 
  
  public Unit(String name, Set<UnitStat> statSheet, UnitGrowth growths) {
    super();
    this.name = name;
    this.statSheet = statSheet;
    this.growths = growths;
    this.id = lastIdentifier.incrementAndGet();
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
