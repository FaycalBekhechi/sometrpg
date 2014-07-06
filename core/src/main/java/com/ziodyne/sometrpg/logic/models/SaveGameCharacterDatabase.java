package com.ziodyne.sometrpg.logic.models;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.util.CollectionUtils;

public class SaveGameCharacterDatabase implements CharacterDatabase {
  private Map<String, Character> characterMap;

  public SaveGameCharacterDatabase(Collection<Character> characters) {
    this.characterMap = CollectionUtils.indexBy(characters, Character::getId);
  }

  @Override
  public Optional<Character> getById(String id) {

    return Optional.ofNullable(characterMap.get(id));
  }

  @Override
  public void save(Character character) {

  }

  @Override
  public Set<Character> getAll() {

    return Sets.newHashSet(characterMap.values());
  }
}
