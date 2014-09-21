package com.ziodyne.sometrpg.view;

public enum AnimationType {
  IDLE("idle"),
  COMBAT_IDLE_NORTH("combat_idle_north"),
  COMBAT_IDLE_SOUTH("combat_idle_south"),
  COMBAT_IDLE_EAST("combat_idle_east"),
  COMBAT_IDLE_WEST("combat_idle_west"),
  ATTACK_NORTH("attack_north"),
  ATTACK_SOUTH("attack_south"),
  ATTACK_EAST("attack_east"),
  ATTACK_WEST("attack_west"),
  DODGE_SOUTH("dodge_south"),
  DODGE_EAST("dodge_east"),
  DODGE_WEST("dodge_west"),
  DODGE_NORTH("dodge_north"),
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
