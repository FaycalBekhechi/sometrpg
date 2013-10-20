package com.ziodyne.sometrpg.logic.models;

public class Tile {
  private Unit occupyingUnit;
  private boolean passable = true;
  private final TerrainType terrainType;
  
  public Tile(TerrainType terrainType) {
    super();
    this.terrainType = terrainType;
  }

  public Unit getOccupyingUnit() {
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

  public void setOccupyingUnit(Unit occupyingUnit) {
    if (isOccupied()){
      throw new IllegalArgumentException("Can't send a unit to an occupied square.");
    }
    
    this.occupyingUnit = occupyingUnit;
  }

  public TerrainType getTerrainType() {
    return terrainType;
  }
}
