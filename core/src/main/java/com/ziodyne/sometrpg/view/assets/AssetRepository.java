package com.ziodyne.sometrpg.view.assets;

/**
 * Read-only cache of assets.
 */
public interface AssetRepository {
  public <T> T get(String filename);
  public <T> T get(String filename, Class<T> type);
  public <T> Iterable<T> getAll(Class<T> type);
}
