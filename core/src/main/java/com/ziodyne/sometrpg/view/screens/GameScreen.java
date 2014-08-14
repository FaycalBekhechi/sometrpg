package com.ziodyne.sometrpg.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.Disposable;

/**
 * Base class for all screens. Provides a single point of asset management and ensures assets are disposed.
 */
public abstract class GameScreen extends ScreenAdapter implements Disposable {
  private final AssetManager assetManager = new AssetManager();

  @Override
  public void render(float delta) {
    Gdx.gl.glClearColor(0, 0, 0, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

    assetManager.update();
  }

  protected AssetManager getAssetManager() {
    return assetManager;
  }

  @Override
  public void dispose() {

    assetManager.dispose();
  }
}
