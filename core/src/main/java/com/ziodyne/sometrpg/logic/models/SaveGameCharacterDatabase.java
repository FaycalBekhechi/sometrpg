package com.ziodyne.sometrpg.logic.models;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class SaveGameCharacterDatabase implements CharacterDatabase {
  private Map<String, Character> characterMap;

  public SaveGameCharacterDatabase(Collection<Character> characters) {
    this.characterMap = characters
      .stream()
      .collect(Collectors.toMap(Character::getId, Function.identity()));
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
