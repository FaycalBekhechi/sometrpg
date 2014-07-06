package com.ziodyne.sometrpg.logic.models;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;
import com.ziodyne.sometrpg.logic.loader.models.Roster;
import com.ziodyne.sometrpg.logic.models.battle.Army;
import com.ziodyne.sometrpg.util.CollectionUtils;

public class SaveGameCharacterDatabase implements CharacterDatabase {
  private Map<String, Character> characterIndex;
  private Map<String, Army> armyIndex;

  public SaveGameCharacterDatabase(Collection<Character> characters, Collection<Roster> rosters) {
    this.characterIndex = CollectionUtils.indexBy(characters, Character::getId);

    this.armyIndex = new HashMap<>();
    for (Roster roster : rosters) {
      Army army = new Army(roster.getName(), roster.getType());
      for (String member : roster.getMembers()) {
        armyIndex.put(member, army);
      }
    }
  }

  @Override
  public Army getArmyForCharacterId(String id) {

    return armyIndex.get(id);
  }

  @Override
  public Optional<Character> getById(String id) {

    return Optional.ofNullable(characterIndex.get(id));
  }

  @Override
  public void save(Character character) {

  }

  @Override
  public Set<Character> getAll() {

    return Sets.newHashSet(characterIndex.values());
  }
}
