package com.ziodyne.sometrpg.logic.loader.loaders;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.util.JsonUtils;
import com.ziodyne.sometrpg.logic.loader.models.Armies;

public class ArmiesLoader extends SynchronousAssetLoader<Armies, ArmiesLoader.ArmiesLoaderParams> {

  public static class ArmiesLoaderParams extends AssetLoaderParameters<Armies> { }

  public ArmiesLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  @Override
  public Armies load(AssetManager assetManager, String s, FileHandle fileHandle,
                     ArmiesLoaderParams armiesLoaderParams) {

    try {
      return JsonUtils.readValue(fileHandle.read(), Armies.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load armies: " + s, e);
    }
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle,
                                                ArmiesLoaderParams armiesLoaderParams) {

    return null;
  }
}
