package com.ziodyne.sometrpg.logic.models.battle;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum ArmyType {
  PLAYER("player"),
  ENEMY("enemy"),
  NEUTRAL("neutral");

  ArmyType(String value) {

    this.value = value;
  }

  private final String value;

  public String getValue() {

    return value;
  }

  @JsonCreator
  public static ArmyType create(String val) {
    for (ArmyType type : ArmyType.values()) {
      if (type.getValue().equalsIgnoreCase(val)) {
        return type;
      }
    }

    return null;
  }
}
