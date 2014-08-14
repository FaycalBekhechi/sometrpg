package com.ziodyne.sometrpg.view.audio;

import com.ziodyne.sometrpg.view.assets.AssetRepository;

public class BattleMusicPlayer {
  private final MusicPlayer musicPlayer;

  public BattleMusicPlayer(AssetRepository repository) {
    musicPlayer = new MusicPlayer(repository);
  }

  public void play(String filename) {
    musicPlayer.play(filename);
  }
}
