package com.ziodyne.sometrpg.view.assets.loaders;

import java.io.IOException;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ziodyne.sometrpg.util.JsonUtils;
import org.apache.commons.io.FilenameUtils;

public class ShaderProgramLoader extends SynchronousAssetLoader<ShaderProgram, ShaderProgramLoader.Params> {
  public static class Params extends AssetLoaderParameters<ShaderProgram> { }

  public ShaderProgramLoader(FileHandleResolver resolver) {

    super(resolver);
  }

  private static class ShaderPointers {
    @JsonProperty("vert")
    public String vertexPath;

    @JsonProperty("frag")
    public String fragmentPath;

  }

  @Override
  public ShaderProgram load(AssetManager assetManager, String fileName, FileHandle file,
                            Params parameter) {


    ShaderPointers pointers;
    try {
      pointers = JsonUtils.readValue(file.read(), ShaderPointers.class);
    } catch (IOException e) {
      throw new RuntimeException("Failed to load shader asset pointers: " + fileName, e);
    }

    String rootPath = FilenameUtils.getPath(fileName);
    String vertexShader = resolve(rootPath + pointers.vertexPath).readString();
    String fragShader = resolve(rootPath + pointers.fragmentPath).readString();

    return new ShaderProgram(vertexShader, fragShader);
  }

  @Override
  public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file,
                                                Params parameter) {

    return null;
  }
}
