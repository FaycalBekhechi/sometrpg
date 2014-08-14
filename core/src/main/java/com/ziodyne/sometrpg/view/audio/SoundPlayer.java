package com.ziodyne.sometrpg.view.audio;

import com.badlogic.gdx.audio.Sound;
import com.ziodyne.sometrpg.view.assets.AssetRepository;

public class SoundPlayer {
  private final AssetRepository assetRepository;

  public SoundPlayer(AssetRepository assetRepository) {

    this.assetRepository = assetRepository;
  }

  public void play(String filename) {

    Sound sound = assetRepository.get(filename, Sound.class);
    sound.play();
  }

  public void resume() {

    for (Sound sound : allSounds()) {
      sound.resume();
    }
  }

  public void pause() {

    for (Sound sound : allSounds()) {
      sound.pause();
    }
  }

  private Iterable<Sound> allSounds() {

    return assetRepository.getAll(Sound.class);
  }
}
