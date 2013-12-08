package com.ziodyne.sometrpg.view.input;

import aurelienribon.tweenengine.TweenManager;
import com.artemis.World;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.logic.models.battle.Battle;

public class BattleMapController extends VoidEntitySystem implements InputProcessor {
  private final OrthographicCamera camera;
  private final TweenManager tweenManager;
  private final Battle battle;
  private final World world;

  public interface Factory {
    public BattleMapController create(OrthographicCamera camera, Battle battle, World world);
  }

  @AssistedInject
  BattleMapController(@Assisted OrthographicCamera camera, @Assisted Battle battle, @Assisted World world, TweenManager tweenManager) {
    this.camera = camera;
    this.tweenManager = tweenManager;
    this.battle = battle;
    this.world = world;
  }

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

  @Override
  protected void processSystem() {
  }
}
