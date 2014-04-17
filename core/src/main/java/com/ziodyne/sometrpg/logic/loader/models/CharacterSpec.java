package com.ziodyne.sometrpg.logic.loader.models;

public class CharacterSpec {
  private String name;
  private CharacterAssets assets;
  private CharacterStats stats;
  private CharacterGrowths growths;

  public String getName() {

    return name;
  }

  public CharacterAssets getAssets() {

    return assets;
  }

  public CharacterStats getStats() {

    return stats;
  }

  public CharacterGrowths getGrowths() {

    return growths;
  }
}
