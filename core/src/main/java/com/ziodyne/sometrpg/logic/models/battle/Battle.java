package com.ziodyne.sometrpg.logic.models.battle;

import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.BattleResult;
import com.ziodyne.sometrpg.logic.models.battle.combat.Encounter;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;
import com.ziodyne.sometrpg.logic.models.battle.combat.CombatantAction;
import com.ziodyne.sometrpg.logic.navigation.Path;
import com.ziodyne.sometrpg.logic.util.GridPoint2;

import java.util.Set;

public interface Battle {

  public BattleMap getMap();

  /**
   * Returns the {@link Army} active for the current turn.
   *
   * @return the active Army for the current turn
   */
  public Army getCurrentTurnArmy();

  /**
   * Returns the {@link Combatant}s owned by the player.
   *
   * @return the Combatants owned by the player
   */
  public Set<Combatant> getPlayerUnits();


  public GridPoint2 getCombatantPosition(Combatant combatant);

  public Combatant getCombatantForCharacter(Character character);

  /**
   * Returns the {@link Combatant}s owned by the enemy.
   *
   * @return ths Combatants owned by the enemy
   */
  public Set<Combatant> getEnemyUnits();

  /**
   * Returns the {@link Combatant}s owned by neither the enemy nor the player.
   *
   * @return the Combatants owned by neither the enemy nor the player
   */
  public Set<Combatant> getNeutralUnits();

  /**
   * Move a {@link Combatant} to another {@link Tile}.
   *  @param combatant The Combatant being moved
   * @param destination The Tile to which to move them
   * @param path
   */
  public void moveCombatant(Combatant combatant, Tile destination, Path<GridPoint2> path);

  /**
   * Have one {@link Combatant} attack another.
   *  @param attacker The Combatant attacking
   * @param attack The {@link com.ziodyne.sometrpg.logic.models.battle.combat.Attack} to perform
   * @param defender The Combatant receiving the attack
   */
  public BattleResult executeAttack(Combatant attacker, Attack attack, Combatant defender);

  public Encounter startCombat(Combatant attacker, Attack attack, Combatant defender);

  /**
   * Return <tt>true</tt> if the battle is won.
   *
   * @return <tt>true</tt> if the battle is won
   */
  public boolean isWon();

  /**
   * Return <tt>true</tt> if the battle is lost.
   *
   * @return <tt>true</tt> if the battle is lost
   */
  public boolean isLost();

  /**
   * Get which actions a combatant can do right now.
   * @param combatant The combatant
   * @return A {@link Set} of {@link CombatantAction}s a Combatant can take.
   */
  public Set<CombatantAction> getAvailableActions(Combatant combatant);

  /**
   * Get the number of squares a combatant can move this turn. (Taking into account squares already moved)
   * @param combatant The Combatant being moved
   * @return The # of squares left to move this combatant has.
   */
  public int getMovementRangeRemaining(Combatant combatant);

}
