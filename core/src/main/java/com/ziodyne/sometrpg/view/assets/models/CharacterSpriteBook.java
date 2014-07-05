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

  private SpriteReference attack;

  private SpriteReference dodge;


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

  public SpriteReference getAttack() {

    return attack;
  }

  public SpriteReference getDodge() {

    return dodge;
  }
}
