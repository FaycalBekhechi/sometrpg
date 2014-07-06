package com.ziodyne.sometrpg.logic.loader.models;

import com.ziodyne.sometrpg.logic.loader.models.CharacterSpec;

import java.util.List;

public class GameSpec {
  private List<CharacterSpec> characters;

  public List<CharacterSpec> getCharacters() {
    return characters;
  }

  public void setCharacters(List<CharacterSpec> characters) {
    this.characters = characters;
  }
}
