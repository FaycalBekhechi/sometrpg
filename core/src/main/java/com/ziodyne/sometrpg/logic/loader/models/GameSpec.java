package com.ziodyne.sometrpg.logic.loader.models;

import java.util.List;

public class GameSpec {
  private List<CharacterSpec> characters;
  private List<Roster> rosters;

  public List<Roster> getRosters() {

    return rosters;
  }

  public void setRosters(List<Roster> rosters) {

    this.rosters = rosters;
  }

  public List<CharacterSpec> getCharacters() {
    return characters;
  }

  public void setCharacters(List<CharacterSpec> characters) {
    this.characters = characters;
  }
}
