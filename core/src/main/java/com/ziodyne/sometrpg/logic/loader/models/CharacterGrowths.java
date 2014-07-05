package com.ziodyne.sometrpg.logic.loader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterGrowths {
  @JsonProperty("hp")
  private float health;

  @JsonProperty("str")
  private float strength;

  @JsonProperty("spd")
  private float speed;

  @JsonProperty("skl")
  private float skill;

  @JsonProperty("def")
  private float defense;

  @JsonProperty("mov")
  private float movement;

  @JsonProperty("lvl")
  private float level;

  public float getHealth() {

    return health;
  }

  public float getStrength() {

    return strength;
  }

  public float getSpeed() {

    return speed;
  }

  public float getSkill() {

    return skill;
  }

  public float getDefense() {

    return defense;
  }

  public float getMovement() {

    return movement;
  }

  public float getLevel() {

    return level;
  }
}
