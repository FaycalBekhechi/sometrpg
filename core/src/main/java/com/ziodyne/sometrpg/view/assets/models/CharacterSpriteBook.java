package com.ziodyne.sometrpg.view.assets.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterSpriteBook {

  @JsonProperty("character_id")
  private String characterId;

  private SpriteReference idle;

  @JsonProperty("run_north")
  private SpriteReference runNorth;

  @JsonProperty("run_south")
  private SpriteReference runSouth;

  @JsonProperty("run_east")
  private SpriteReference runEast;

  @JsonProperty("run_west")
  private SpriteReference runWest;

  @JsonProperty("attack_east")
  private SpriteReference attackEast;

  @JsonProperty("attack_west")
  private SpriteReference attackWest;

  @JsonProperty("attack_south")
  private SpriteReference attackSouth;

  @JsonProperty("attack_north")
  private SpriteReference attackNorth;

  private SpriteReference dodge;

  public String getCharacterId() {

    return characterId;
  }

  public SpriteReference getIdle() {

    return idle;
  }

  public SpriteReference getRunNorth() {

    return runNorth;
  }

  public SpriteReference getRunSouth() {

    return runSouth;
  }

  public SpriteReference getRunEast() {

    return runEast;
  }

  public SpriteReference getRunWest() {

    return runWest;
  }

  public SpriteReference getAttackEast() {

    return attackEast;
  }

  public SpriteReference getAttackWest() {

    return attackWest;
  }

  public SpriteReference getAttackSouth() {

    return attackSouth;
  }

  public SpriteReference getAttackNorth() {

    return attackNorth;
  }

  public SpriteReference getDodge() {

    return dodge;
  }
}
