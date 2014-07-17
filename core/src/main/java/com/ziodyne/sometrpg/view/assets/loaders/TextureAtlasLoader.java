package com.ziodyne.sometrpg.view.assets.loaders;

import java.io.IOException;
import java.util.List;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.ziodyne.sometrpg.util.JsonUtils;

public class TextureAtlasLoader extends SynchronousAssetLoader<TextureAtlas, TextureAtlasLoader.Parameter> {

  public TextureAtlasLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  private String textureFilename;

  private List<JsonNode> regions;

  private Integer regionHeight;
  private Integer regionWidth;

  @Override
  public TextureAtlas load(AssetManager assetManager, String fileName, FileHandle file,
                           Parameter parameter) {

    Texture tex = assetManager.get(textureFilename, Texture.class);
    TextureAtlas atlas = new TextureAtlas();

    for (JsonNode regionNode : regions) {

      int x = regionNode.get("x").asInt();
      int y = regionNode.get("y").asInt();
      int height = regionHeight;
      int width = regionWidth;

      JsonNode localHeight = regionNode.path("height");
      if (!localHeight.isMissingNode()) {
        height = localHeight.asInt();
      }

      JsonNode localWidth = regionNode.path("width");
      if (!localWidth.isMissingNode()) {
        width = localWidth.asInt();
      }

      TextureRegion region = new TextureRegion(tex, x*width, y*height, width, height);
      String name = regionNode.get("name").asText();

      atlas.addRegion(name, region);
    }

    return atlas;
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
                                                Parameter parameter) {

    JsonNode json;
    try {
      json = JsonUtils.readTree(file.read());
    } catch (IOException e) {
      throw new RuntimeException("Failed to read character sprites from: " + fileName, e);
    }

    textureFilename = json.get("filename").asText();
    regions = Lists.newArrayList(json.get("regions").elements());

    JsonNode height = json.path("region_height");
    if (!height.isMissingNode()) {
      regionHeight = height.asInt();
    }

    JsonNode width = json.path("region_width");
    if (!width.isMissingNode()) {
      regionWidth = width.asInt();
    }


    Array<AssetDescriptor> descriptors = new Array<>();
    descriptors.add(new AssetDescriptor<>(textureFilename, Texture.class));

    return descriptors;
  }

  public static class Parameter extends AssetLoaderParameters<TextureAtlas> { }
}
