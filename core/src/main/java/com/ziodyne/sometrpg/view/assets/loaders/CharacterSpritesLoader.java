package com.ziodyne.sometrpg.view.assets.loaders;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.ziodyne.sometrpg.logic.loader.models.SpriteSheet;
import com.ziodyne.sometrpg.util.CollectionUtils;
import com.ziodyne.sometrpg.util.JsonUtils;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.assets.models.CharacterSpriteBook;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;
import com.ziodyne.sometrpg.view.entities.UnitEntityAnimation;
import org.apache.commons.io.FilenameUtils;

public class CharacterSpritesLoader extends SynchronousAssetLoader<CharacterSprites, CharacterSpritesLoader.CharacterSpritesParams> {

  public static class CharacterSpritesParams extends AssetLoaderParameters<CharacterSprites> { }

  public CharacterSpritesLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  private Map<String, Set<String>> animationNames = new HashMap<>();
  private Map<String, String> pathsByName = new HashMap<>();
  private Set<String> spriteSheetPaths = new HashSet<>();
  private Map<String, AnimationType> types = new HashMap<>();

  @Override
  public CharacterSprites load(AssetManager assetManager, String fileName, FileHandle fileHandle,
                               CharacterSpritesParams characterSpritesParams) {

    CharacterSprites sprites = new CharacterSprites();

    Map<String, Texture> texturesByPath = spriteSheetPaths.stream()
      .collect(Collectors.toMap(
        Function.identity(),
        path -> assetManager.get(path.replace(".json", ".png"), Texture.class)
      ));

    Map<String, SpriteSheet> sheetsByFilename = spriteSheetPaths.stream()
      .collect(Collectors.toMap(
        Function.identity(),
        path -> assetManager.get(path, SpriteSheet.class)
      ));


    Map<String, Set<UnitEntityAnimation>> result = new HashMap<>();
    animationNames.keySet().stream()
      .forEach((charName) -> {
        result.put(charName, animationNames.get(charName).stream().map((name) -> {

          String path = pathsByName.get(name);
          Texture tex = texturesByPath.get(path);
          SpriteSheet sheet = sheetsByFilename.get(path);
          AnimationType type = types.get(path);
          int gridSize = sheet.getGridSize();

          return new UnitEntityAnimation(tex, type, sheet.getAnimationSpecs().get(name), gridSize);
        }).collect(Collectors.toSet()));
      });

    sprites.setAnimationsByCharacterId(result);

    return sprites;
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String s, FileHandle fileHandle,
                                                CharacterSpritesParams characterSpritesParams) {

    JsonNode json;
    try {
      json = JsonUtils.readTree(fileHandle.read());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read character sprites from: " + s, e);
    }

    String basePath = FilenameUtils.getFullPath(s);

    List<JsonNode> sprite = Lists.newArrayList(json.get("sprites"));
    sprite.stream().forEach((node) -> {
      String charId = node.get("character_id").asText();
      Set<String> animNames = new HashSet<>();
      Lists.newArrayList(node.fieldNames()).stream()
        .filter(field -> !field.equals("character_id"))
        .forEach((animFieldName) -> {
          JsonNode anim = node.get(animFieldName);
          String animName = anim.get("name").asText();
          String sheetPath = basePath + anim.get("sheet").asText();
          types.put(
            sheetPath,
            AnimationType.fromString(animFieldName)
          );

          pathsByName.put(
            animName,
            sheetPath
          );

          animNames.add(animName);
        });

      animationNames.put(charId, animNames);
    });

    sprite.stream()
      .forEach((spec) -> {
         Lists.newArrayList(spec).stream()
           .filter(node -> node.has("sheet"))
           .map(node -> basePath + node.get("sheet").asText())
           .forEach(spriteSheetPaths::add);
      });


    Array<AssetDescriptor> result = new Array<>();

    spriteSheetPaths.forEach(path -> result.add(new AssetDescriptor<>(path, SpriteSheet.class)));

    return result;
  }
}
