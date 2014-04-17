package com.ziodyne.sometrpg.logic.loader.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CharacterAssets {
  @JsonProperty("portrait")
  private String portraitFilename;
  private CharacterAnimations animations;
}
