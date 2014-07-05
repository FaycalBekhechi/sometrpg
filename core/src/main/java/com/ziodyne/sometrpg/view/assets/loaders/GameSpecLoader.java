package com.ziodyne.sometrpg.view.assets.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.ziodyne.sometrpg.logic.loader.models.Characters;
import com.ziodyne.sometrpg.util.JsonUtils;
import com.ziodyne.sometrpg.view.assets.GameSpec;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class GameSpecLoader extends SynchronousAssetLoader<GameSpec, GameSpecLoader.GameParameters> {

  public static class GameParameters extends AssetLoaderParameters<GameSpec> {}

  public GameSpecLoader(FileHandleResolver resolver) {
    super(resolver);
  }

  @Override
  public GameSpec load(AssetManager assetManager, String fileName, FileHandle file, GameParameters parameter) {
    GameSpec spec = new GameSpec();

    JsonNode gameJson;
    try {
      gameJson = JsonUtils.readTree(file.read());
    } catch (IOException e) {
      throw new RuntimeException("Cannot load game json file: " + fileName, e);
    }

    String characterPath = gameJson.get("charactersPath").asText();
    if (characterPath == null) {
      throw new RuntimeException("Game missing character file path.");
    }

    String rootPath = FilenameUtils.getFullPath(fileName);
    File characterFile = new File(FilenameUtils.concat(rootPath, characterPath));
    try {
      Characters characters = JsonUtils.readValue(new FileInputStream(characterFile), Characters.class);
      spec.setCharacters(characters.getCharacters());
    } catch (IOException e) {
      throw new RuntimeException("Failed to load character spec list", e);
    }

    return spec;
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, GameParameters parameter) {
    return null;
  }
}
