package com.ziodyne.sometrpg.view.assets.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SpriteReference {

  private String name;

  @JsonProperty("sheet")
  private String sheetPath;

  public String getName() {

    return name;
  }

  public String getSheetPath() {

    return sheetPath;
  }
}
