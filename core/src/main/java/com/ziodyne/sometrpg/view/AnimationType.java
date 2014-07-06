package com.ziodyne.sometrpg.view;

public enum AnimationType {
  IDLE("idle"),
  ATTACK("attack"),
  DODGE("dodge"),
  BE_HIT("be_hit"),
  RUN_NORTH("run_north"),
  RUN_SOUTH("run_south"),
  RUN_EAST("run_east"),
  RUN_WEST("run_west");

  private final String name;

  AnimationType(String name) {

    this.name = name;
  }

  public String getName() {

    return name;
  }

  public static AnimationType fromString(String string) {
    for (AnimationType type : values()) {
      if (type.getName().equals(string)) {
        return type;
      }
    }

    return null;
  }
}
