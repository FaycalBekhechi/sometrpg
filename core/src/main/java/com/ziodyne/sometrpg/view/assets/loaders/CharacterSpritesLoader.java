package com.ziodyne.sometrpg.view.assets.loaders;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.util.JsonUtils;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;

public class CharacterSpritesLoader extends SynchronousAssetLoader<CharacterSprites, CharacterSpritesLoader.CharacterSpritesParams> {

  public static class CharacterSpritesParams extends AssetLoaderParameters<CharacterSprites> { }

  public CharacterSpritesLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  @Override
  public CharacterSprites load(AssetManager assetManager, String fileName, FileHandle fileHandle,
                               CharacterSpritesParams characterSpritesParams) {

    try {
      return JsonUtils.readValue(fileHandle.read(), CharacterSprites.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load character sprites: " + fileName, e);
    }
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle,
                                                CharacterSpritesParams characterSpritesParams) {

    return null;
  }
}
