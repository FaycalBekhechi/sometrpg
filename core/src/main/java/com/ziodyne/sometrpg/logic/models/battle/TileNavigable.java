package com.ziodyne.sometrpg.logic.models.battle;

import com.badlogic.gdx.math.GridPoint2;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

import java.util.Set;

public interface TileNavigable {
  public Set<Tile> getMovableTiles(Combatant combatant);
  public Tile getTile(GridPoint2 point);
  public boolean tileExists(GridPoint2 point);
}
