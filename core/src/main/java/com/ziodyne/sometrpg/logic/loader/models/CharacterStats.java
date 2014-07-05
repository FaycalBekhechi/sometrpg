package com.ziodyne.sometrpg.logic.loader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterStats {
  @JsonProperty("hp")
  private int health;

  @JsonProperty("str")
  private int strength;

  @JsonProperty("spd")
  private int speed;

  @JsonProperty("skl")
  private int skill;

  @JsonProperty("def")
  private int defense;

  @JsonProperty("mov")
  private int movement;

  @JsonProperty("lvl")
  private int level;

  public int getHealth() {

    return health;
  }

  public int getStrength() {

    return strength;
  }

  public int getSpeed() {

    return speed;
  }

  public int getSkill() {

    return skill;
  }

  public int getDefense() {

    return defense;
  }

  public int getMovement() {

    return movement;
  }

  public int getLevel() {

    return level;
  }
}
