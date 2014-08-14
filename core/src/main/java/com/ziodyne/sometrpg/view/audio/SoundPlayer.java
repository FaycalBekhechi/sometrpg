package com.ziodyne.sometrpg.view.audio;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class SoundPlayer {
  private final AssetManager assetManager;

  public SoundPlayer(AssetManager assetManager) {

    this.assetManager = assetManager;
  }

  public void play(String filename) {
    Sound sound = assetManager.get(filename, Sound.class);
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

    Array<Sound> sounds = new Array<>();
    assetManager.getAll(Sound.class, sounds);

    return sounds;
  }
}
