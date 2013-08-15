package com.ziodyne.sometrpg.models;

public class Tile {
  private Unit occupyingUnit;
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
