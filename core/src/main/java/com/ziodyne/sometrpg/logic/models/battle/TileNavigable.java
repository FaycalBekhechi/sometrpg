package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Pathfinder;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

import java.util.Set;

public interface TileNavigable {
  public Set<Tile> getMovableTiles(Combatant combatant);
  public Tile getTile(GridPoint2 point);
  public boolean tileExists(GridPoint2 point);
  public Pathfinder<GridPoint2> createPathfinder();
  public Pathfinder<GridPoint2> createCachedPathfinder();
}
