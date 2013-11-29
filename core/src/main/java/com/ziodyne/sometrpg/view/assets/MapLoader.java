package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.ziodyne.sometrpg.logic.models.battle.BattleMap;

public class MapLoader extends SynchronousAssetLoader<BattleMap, MapLoader.MapParameter> {
  public MapLoader(FileHandleResolver resolver) {
    super(resolver);
  }

  @Override
  public BattleMap load(AssetManager assetManager, String fileName, FileHandle file, MapParameter parameter) {
    TiledMap tiledMap = assetManager.get(fileName, TiledMap.class);
    return Conversion.convert(tiledMap);
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, MapParameter parameter) {
    Array<AssetDescriptor> deps =  new Array<AssetDescriptor>();
    deps.add(new AssetDescriptor<TiledMap>(file, TiledMap.class));

    return deps;
  }

  public static class MapParameter extends AssetLoaderParameters<BattleMap> { }
}
