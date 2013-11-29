package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;
import com.ziodyne.sometrpg.logic.models.battle.Battle;
import org.apache.commons.io.FilenameUtils;

public class BattleLoader extends SynchronousAssetLoader<Battle, BattleLoader.BattleParameter> {
  // Defer to the core deserializer for the heavy lifting.
  private final com.ziodyne.sometrpg.logic.loader.BattleLoader battleLoader;

  public BattleLoader(FileHandleResolver resolver) {
    super(resolver);
    this.battleLoader = new com.ziodyne.sometrpg.logic.loader.BattleLoader();
  }

  @Override
  public Battle load(AssetManager assetManager, String fileName, FileHandle file, BattleParameter parameter) {
    BattleMap map = assetManager.get(getTmxFilepath(fileName), BattleMap.class);
    return battleLoader.load(map, file.file());
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, BattleParameter parameter) {
    Array<AssetDescriptor> deps = new Array<AssetDescriptor>();
    deps.add(new AssetDescriptor<BattleMap>(getTmxFilepath(fileName), BattleMap.class));

    return deps;
  }

  private static String getTmxFilepath(String fileName) {
    return "maps/" + FilenameUtils.getBaseName(fileName) + "/" + getTmxFilename(fileName);
  }
  private static String getTmxFilename(String fileName) {
    return FilenameUtils.getBaseName(fileName) + ".tmx";
  }

  public static class BattleParameter extends AssetLoaderParameters<Battle> { }
}
