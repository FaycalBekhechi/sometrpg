package com.ziodyne.sometrpg.view.assets;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.logic.loader.models.SpriteSheet;
import com.ziodyne.sometrpg.util.JsonUtils;

public class SpriteSheetAssetLoader extends SynchronousAssetLoader<SpriteSheet, SpriteSheetAssetLoader.SpriteSheetParams> {

  public static class SpriteSheetParams extends AssetLoaderParameters<SpriteSheet> { }

  public SpriteSheetAssetLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  @Override
  public SpriteSheet load(AssetManager assetManager, String fileName, FileHandle file, SpriteSheetParams parameter) {

    try {
      return JsonUtils.readValue(file.read(), SpriteSheet.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load spirte sheet: " + fileName, e);
    }
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, SpriteSheetParams parameter) {

    return null;
  }
}
