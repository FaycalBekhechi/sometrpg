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
import com.ziodyne.sometrpg.util.JsonUtils;
import com.ziodyne.sometrpg.view.AnimationType;
import com.ziodyne.sometrpg.view.assets.models.CharacterSprites;
import com.ziodyne.sometrpg.view.entities.UnitEntityAnimation;
import org.apache.commons.io.FilenameUtils;

public class CharacterSpritesLoader extends SynchronousAssetLoader<CharacterSprites, CharacterSpritesLoader.CharacterSpritesParams> {

  public static class CharacterSpritesParams extends AssetLoaderParameters<CharacterSprites> { }

  public CharacterSpritesLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  // TODO: Pull these into a 'sprite animation data' class
  private Map<String, Set<String>> animNamesByCharacterId = new HashMap<>();
  private Map<String, String> pathsByAnimationName = new HashMap<>();
  private Set<String> uniquePaths = new HashSet<>();
  private Map<String, AnimationType> typesByAnimName = new HashMap<>();

  @Override
  public CharacterSprites load(AssetManager assetManager, String fileName, FileHandle fileHandle,
                               CharacterSpritesParams characterSpritesParams) {

    CharacterSprites sprites = new CharacterSprites();

    // Pull all necessary textures and index them by their sprite sheet's path
    Map<String, Texture> texturesByPath = uniquePaths.stream()
      .collect(Collectors.toMap(
        Function.identity(),
        (path) -> {
          SpriteSheet sheet = assetManager.get(path, SpriteSheet.class);
          String sheetFilePath = sheet.getFileName();

          if (sheetFilePath != null) {
            return assetManager.get(sheetFilePath, Texture.class);
          } else {
            return assetManager.get(path.replace(".json", ".png"), Texture.class);
          }
        }
      ));

    // Pull all necessary sprite sheets and index them by their path
    Map<String, SpriteSheet> sheetsByFilename = uniquePaths.stream()
      .collect(Collectors.toMap(
        Function.identity(),
        path -> assetManager.get(path, SpriteSheet.class)
      ));


    Map<String, Set<UnitEntityAnimation>> result = new HashMap<>();

    // Iterate over each character name
    animNamesByCharacterId.keySet().stream()
      .forEach((charName) -> {
        // Convert each animation name to a UnitEntityAnimation
        result.put(charName, animNamesByCharacterId.get(charName).stream().map((name) -> {

          // Map together all the dumb pieces of data starting with the animation name
          String path = pathsByAnimationName.get(name);
          Texture tex = texturesByPath.get(path);
          SpriteSheet sheet = sheetsByFilename.get(path);
          AnimationType type = typesByAnimName.get(name);
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
    Set<String> staticTexturePaths = new HashSet<>();

    // For each sprite...
    List<JsonNode> sprite = Lists.newArrayList(json.get("sprites"));
    sprite.stream().forEach((node) -> {

      String charId = node.get("character_id").asText();
      Set<String> animNames = new HashSet<>();

      // Iterate over every field except the "character_id"
      Lists.newArrayList(node.fieldNames()).stream()
        .filter(field -> !field.equals("character_id"))
        .forEach((animFieldName) -> {
          JsonNode anim = node.get(animFieldName);
          if (anim.has("texture")) {
            String texPath = basePath + anim.get("texture").asText();
            staticTexturePaths.add(texPath);
          } else {
            String animName = anim.get("name").asText();
            String sheetPath = basePath + anim.get("sheet").asText();

            // Infer the animation type from the name
            typesByAnimName.put(
                    animName,
                    AnimationType.fromString(animFieldName)
            );

            // Keep the path to the sprite sheet and texture indexed by the animation name
            pathsByAnimationName.put(
                    animName,
                    sheetPath
            );

            animNames.add(animName);
          }
        });

      // Collect all the animation names for a single character
      animNamesByCharacterId.put(charId, animNames);
    });

    // Collect each unique path
    sprite.stream()
      .forEach((spec) -> {
         Lists.newArrayList(spec).stream()
           .filter(node -> node.has("sheet"))
           .map(node -> basePath + node.get("sheet").asText())
           .forEach(uniquePaths::add);
      });


    Array<AssetDescriptor> result = new Array<>();

    uniquePaths.forEach(path -> result.add(new AssetDescriptor<>(path, SpriteSheet.class)));
    staticTexturePaths.forEach(path -> result.add(new AssetDescriptor<>(path, Texture.class)));

    return result;
  }
}
