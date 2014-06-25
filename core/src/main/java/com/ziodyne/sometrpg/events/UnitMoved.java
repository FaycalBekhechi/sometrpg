package com.ziodyne.sometrpg.events;

import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

/**
 * An event describing a unit having moved on the map.
 */
public class UnitMoved {

  private final Combatant combatant;
  private final GridPoint2 start;
  private final Path<GridPoint2> path;

  public UnitMoved(Combatant combatant, GridPoint2 start, Path<GridPoint2> path) {

    this.combatant = combatant;
    this.start = start;
    this.path = path;
  }

  public GridPoint2 getStart() {

    return start;
  }

  public Combatant getCombatant() {

    return combatant;
  }

  public Path<GridPoint2> getPath() {

    return path;
  }
}
