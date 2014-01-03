package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;


/**
 * A loader for asset .bundle files. All loading of individual assets is handled by your provided asset manager.
 */
public class AssetBundleLoader {

  public interface Factory {
    public AssetBundleLoader create(AssetManager assetManager, String bundlePath);
  }

  private final AssetManager assetManager;

  private final String bundlePath;

  private final ObjectMapper objectMapper;

  private boolean isLoading;

  private Set<Asset<?>> remainingAssets = new HashSet<Asset<?>>();

  private int totalNumAssets;

  @AssistedInject
  AssetBundleLoader(ObjectMapper mapper, @Assisted AssetManager assetManager, @Assisted String bundlePath) {
    this.objectMapper = mapper;
    this.assetManager = assetManager;
    this.bundlePath = bundlePath;
  }

  /**
   * Load the bundle and enqueue its dependencies for loading.
   *
   * @throws IOException
   */
  public void load() throws IOException {
    if (isLoading) {
      return;
    }

    this.isLoading = true;
    FileHandle bundleFile = Gdx.files.internal(bundlePath);
    InputStream fileStream = bundleFile.read();

    Set<Asset<?>> assets = objectMapper.readValue(fileStream, new TypeReference<Set<Asset<?>>>() { });
    totalNumAssets = assets.size();
    System.out.println("Loading asset bundle with: " + totalNumAssets + " assets.");
    for (Asset<?> asset : assets) {
      remainingAssets.add(asset);
      assetManager.load(asset.getPath(), asset.getClazz());
    }
  }

  /**
   * Like {@link com.badlogic.gdx.assets.AssetManager}'s update, this must be called to continue loading.
   *
   * @see {@link com.badlogic.gdx.assets.AssetManager#update()}
   */
  public boolean update() {
    boolean doneLoading = assetManager.update();

    removeLoadedAssets();
    this.isLoading = doneLoading;

    return doneLoading;
  }


  /**
   * Get the % of assets loaded
   * @return a value from [0.0, 1.0] describing how many assets have been loaded as a ratio of the total
   */
  public float getPercentLoaded() {
    if (totalNumAssets == 0) {
      return 1.0f;
    }

    int numRemaining = remainingAssets.size();
    return 1.0f - (numRemaining / totalNumAssets);
  }

  /**
   * Block the current thread until all assets are loaded.
   */
  public void finishLoading() {
    assetManager.finishLoading();
  }

  /**
   * Removes any assets from the remaining assets list if they have been loaded.
   */
  private void removeLoadedAssets() {
    Iterator<Asset<?>> assetIterator = remainingAssets.iterator();
    while (assetIterator.hasNext()) {
      Asset<?> nextAsset = assetIterator.next();
      if (isLoaded(nextAsset)) {
        assetIterator.remove();
      }
    }
  }

  private boolean isLoaded(Asset<?> asset) {
    return assetManager.isLoaded(asset.getPath(), asset.getClazz());
  }
}
