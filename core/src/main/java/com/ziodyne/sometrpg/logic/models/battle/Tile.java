package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public class Tile {
  private Combatant occupyingUnit;
  private boolean passable = true;
  private final TerrainType terrainType;
  
  public Tile(TerrainType terrainType) {
    super();
    this.terrainType = terrainType;
  }

  public Combatant getOccupyingUnit() {
    return occupyingUnit;
  }
  
  public boolean isOccupied() {
    return occupyingUnit != null;
  }

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public void setOccupyingUnit(Combatant occupyingUnit) {
    if (occupyingUnit != null && isOccupied()){
      throw new IllegalArgumentException("Can't send a unit to an occupied square.");
    }
    
    this.occupyingUnit = occupyingUnit;
  }

  public TerrainType getTerrainType() {
    return terrainType;
  }
}
