package com.ziodyne.sometrpg.logic.models;

import java.util.Optional;
import java.util.Set;

import com.ziodyne.sometrpg.view.screens.debug.ModelTestUtils;

public class DummyCharacterDatabase implements CharacterDatabase {

  @Override
  public Optional<Character> getById(String id) {

    return Optional.of(new Character(ModelTestUtils.homogeneousStats(40), ModelTestUtils.createGrowth(), ModelTestUtils.homogeneousStats(
      20), "Test3x"));
  }

  @Override
  public void save(Character character) {

  }

  @Override
  public Set<Character> getAll() {

    return null;
  }
}
