package com.ziodyne.sometrpg.view.assets.loaders;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.ziodyne.sometrpg.util.JsonUtils;
import com.ziodyne.sometrpg.view.assets.models.Chapter;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;

public class ChapterLoader extends SynchronousAssetLoader<Chapter, ChapterLoader.Parameter> {

  private String spritesFilename;
  private String mapFilename;

  public ChapterLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  @Override
  public Chapter load(AssetManager assetManager, String fileName, FileHandle file, Parameter parameter) {

    TiledMap map = assetManager.get(mapFilename, TiledMap.class);
    CharacterSprites sprites = assetManager.get(spritesFilename, CharacterSprites.class);

    return new Chapter(sprites, map);
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, Parameter parameter) {

    JsonNode json;
    try {
      json = JsonUtils.readTree(file.read());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read chapter from: " + fileName);
    }

    spritesFilename = json.get("sprites").asText();
    mapFilename = json.get("map").asText();

    AssetDescriptor<TiledMap> map = new AssetDescriptor<>(mapFilename, TiledMap.class);
    AssetDescriptor<CharacterSprites> sprites = new AssetDescriptor<>(spritesFilename, CharacterSprites.class);

    Array<AssetDescriptor> descriptors = new Array<>();
    descriptors.add(map);
    descriptors.add(sprites);

    return descriptors;
  }

  public static class Parameter extends AssetLoaderParameters<Chapter> {}
}
