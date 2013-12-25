package com.ziodyne.sometrpg.logic.models.battle;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public class Tile {
  private Combatant combatant;
  private boolean passable = true;
  private final TerrainType terrainType;
  private final GridPoint2 position;
  
  public Tile(TerrainType terrainType, int row, int col) {
    super();
    this.terrainType = terrainType;
    this.position = new GridPoint2(row, col);
  }

  public GridPoint2 getPosition() {
    return position;
  }

  public Combatant getCombatant() {
    return combatant;
  }
  
  public boolean isOccupied() {
    return combatant != null;
  }

  public boolean isPassable() {
    return passable;
  }

  public void setPassable(boolean passable) {
    this.passable = passable;
  }

  public void setCombatant(Combatant combatant) {
    if (combatant != null && isOccupied()){
      throw new IllegalArgumentException("Can't send a unit to an occupied square.");
    }
    
    this.combatant = combatant;
  }

  public TerrainType getTerrainType() {
    return terrainType;
  }
}
