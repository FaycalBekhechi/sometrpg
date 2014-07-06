package com.ziodyne.sometrpg.view.assets.models;

import java.util.Set;

import com.ziodyne.sometrpg.logic.models.battle.ArmyType;

public class ArmySpec {
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
