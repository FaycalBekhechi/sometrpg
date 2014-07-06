package com.ziodyne.sometrpg.logic.loader.models;

import java.util.Set;

import com.ziodyne.sometrpg.logic.models.battle.ArmyType;

public class Roster {
  private String name;
  private Set<String> members;
  private ArmyType type;

  public String getName() {

    return name;
  }

  public Set<String> getMembers() {

    return members;
  }

  public ArmyType getType() {

    return type;
  }
}
