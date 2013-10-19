package com.ziodyne.sometrpg.input;

import com.badlogic.gdx.InputProcessor;

/**
 * This is an input processor that does nothing, providing null implementations
 * of each method of {@link InputProcessor}. Rarely do classes need to do
 * anything special for EVERY type of input, so this will allow classes
 * to override default implementations for only the pieces they need.
 */
public class DefaultInputProcessor implements InputProcessor {
  @Override
  public boolean keyDown(int keycode) {
    return false;
  }

  @Override
  public boolean keyUp(int keycode) {
    return false;
  }

  @Override
  public boolean keyTyped(char character) {
    return false;
  }

  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchUp(int screenX, int screenY, int pointer, int button) {
    return false;
  }

  @Override
  public boolean touchDragged(int screenX, int screenY, int pointer) {
    return false;
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {
    return false;
  }

  @Override
  public boolean scrolled(int amount) {
    return false;
  }
}
