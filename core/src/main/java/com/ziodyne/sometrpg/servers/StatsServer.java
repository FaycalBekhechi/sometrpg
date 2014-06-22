package com.ziodyne.sometrpg.servers;

import java.util.List;

import com.ziodyne.sometrpg.logic.models.Character;
import com.ziodyne.sometrpg.logic.models.battle.combat.Combatant;

public interface StatsServer {

  public List<Character> getCharacters();

  public void applyDamage(Combatant combatant, int damage);
}
