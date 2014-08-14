package com.ziodyne.sometrpg.view.audio;

import com.badlogic.gdx.audio.Music;
import com.ziodyne.sometrpg.view.assets.AssetRepository;

class MusicPlayer implements AudioPlayer {
  private final AssetRepository assetRepository;
  private Music playing;

  public MusicPlayer(AssetRepository assetRepository) {

    this.assetRepository = assetRepository;
  }

  public void play(String filename) {
    Music music = assetRepository.get(filename, Music.class);

    if (playing != null) {
      playing.stop();
    }

    music.setLooping(true);
    music.play();
    playing = music;
  }

  public void pause() {
    if (playing != null) {
      playing.pause();
    }
  }

  public void resume() {
    if (playing != null) {
      playing.play();
    }
  }
}
