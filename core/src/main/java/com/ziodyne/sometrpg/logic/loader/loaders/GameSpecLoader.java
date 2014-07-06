package com.ziodyne.sometrpg.logic.loader.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.ziodyne.sometrpg.logic.loader.models.Armies;
import com.ziodyne.sometrpg.logic.loader.models.Characters;
import com.ziodyne.sometrpg.logic.loader.models.GameSpec;
import com.ziodyne.sometrpg.util.JsonUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.IOException;

public class GameSpecLoader extends SynchronousAssetLoader<GameSpec, GameSpecLoader.GameParameters> {

  public static class GameParameters extends AssetLoaderParameters<GameSpec> {}

  public GameSpecLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  private String fullCharactersPath;
  private String fullRostersPath;

  @Override
  public GameSpec load(AssetManager assetManager, String fileName, FileHandle file, GameParameters parameter) {
    GameSpec spec = new GameSpec();

    spec.setCharacters(assetManager.get(fullCharactersPath, Characters.class).getCharacters());
    spec.setRosters(assetManager.get(fullRostersPath, Armies.class).getArmies());

    return spec;
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, GameParameters parameter) {

    JsonNode gameSpecJson;
    try {
      gameSpecJson = JsonUtils.readTree(file.read());
    } catch (IOException e) {
      throw new RuntimeException("Cannot load game json file: " + fileName, e);
    }

    Array<AssetDescriptor> deps = new Array<>();


    String pathRoot = FilenameUtils.getFullPath(fileName);

    String characterPath = gameSpecJson.get("charactersPath").asText();
    if (characterPath == null) {
      throw new RuntimeException("Game missing character file path.");
    }
    fullCharactersPath = pathRoot + characterPath;
    deps.add(new AssetDescriptor<>(fullCharactersPath, Characters.class));

    String rosterPath = gameSpecJson.get("armiesPath").asText();
    if (rosterPath == null) {
      throw new RuntimeException("Game missing roster file path.");
    }
    fullRostersPath = pathRoot + rosterPath;
    deps.add(new AssetDescriptor<>(fullRostersPath, Armies.class));

    return deps;
  }
}
