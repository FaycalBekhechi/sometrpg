package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;

/**
 * An {@link AssetRepository} that provides read-only access to an {@link AssetManager}.
 *
 * Passing around the asset manager directory spreads too much power, but it's also the source
 * of all assets in the system.
 */
public class AssetManagerRepository implements AssetRepository {
  private final AssetManager assetManager;

  public AssetManagerRepository(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  @Override
  public <T> T get(String fileName) {
    return assetManager.get(fileName);
  }

  @Override
  public <T> T get(String fileName, Class<T> type) {
    return assetManager.get(fileName, type);
  }

  @Override
  public <T> Iterable<T> getAll(Class<T> type) {

    Array<T> allItems = new Array<>();
    assetManager.getAll(type, allItems);
    return allItems;
  }
}
