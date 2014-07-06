package com.ziodyne.sometrpg.logic.models;

import java.util.Optional;
import java.util.Set;

import com.ziodyne.sometrpg.logic.models.battle.Army;

public interface CharacterDatabase {

  public Army getArmyForCharacterId(String id);

  public Optional<Character> getById(String id);

  public void save(Character character);

  public Set<Character> getAll();
}
