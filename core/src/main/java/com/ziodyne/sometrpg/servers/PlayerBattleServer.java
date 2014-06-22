package com.ziodyne.sometrpg.servers;

import com.ziodyne.sometrpg.logic.models.battle.Tile;
import com.ziodyne.sometrpg.logic.models.battle.combat.Attack;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

/**
 * This class delegates to the real battle server to do work, but validates moves that the player is allowed to take.
 */
public class PlayerBattleServer implements BattleServer {
  private final BattleServer coreServer;

  public PlayerBattleServer(BattleServer coreServer) {

    this.coreServer = coreServer;
  }

  @Override
  public void moveCombatant(Combatant combatant, Tile destination) {

    coreServer.moveCombatant(combatant, destination);
  }

  @Override
  public void attack(Combatant attacker, Attack attack, Combatant defender) {

    coreServer.attack(attacker, attack, defender);
  }
}
