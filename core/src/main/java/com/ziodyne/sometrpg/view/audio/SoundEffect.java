package com.ziodyne.sometrpg.view.audio;

public enum SoundEffect {
  HIT("sounds/hit.mp3");

  private final String path;

  SoundEffect(String path) {

    this.path = path;
  }

  public String getPath() {

    return path;
  }
}
