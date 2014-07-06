package com.ziodyne.sometrpg.logic.loader;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.logic.loader.models.Characters;
import com.ziodyne.sometrpg.util.JsonUtils;

public class CharactersLoader extends SynchronousAssetLoader<Characters, CharactersLoader.Parameters> {
  public static class Parameters extends AssetLoaderParameters<Characters> { }

  public CharactersLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  @Override
  public Characters load(AssetManager assetManager, String fileName, FileHandle fileHandle,
                            Parameters parameters) {

    try {
      return JsonUtils.readValue(fileHandle.read(), Characters.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load characters: " + fileName, e);
    }
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle,
                                                Parameters parameters) {

    return null;
  }
}
