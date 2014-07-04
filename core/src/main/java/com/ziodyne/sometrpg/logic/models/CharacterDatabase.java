package com.ziodyne.sometrpg.logic.models;

import java.util.Optional;
import java.util.Set;

public interface CharacterDatabase {

  public Optional<Character> getById(String id);

  public void save(Character character);

  public Set<Character> getAll();
}
