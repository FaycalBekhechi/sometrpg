package com.ziodyne.sometrpg.view.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.ziodyne.sometrpg.view.Director;

public class GameExitController extends InputAdapter {
  private final Director director;

  public GameExitController(Director director) {
    this.director = director;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    if (button == Input.Buttons.RIGHT) {
      director.popScreen();
      return true;
    }
    return false;
  }
}
