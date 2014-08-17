package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.loaders.BitmapFontLoader;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class BitmapFontAsset extends Asset<BitmapFont> {
  public BitmapFontAsset() {
    super("bitmap_font", BitmapFont.class);
  }

  @Override
  public AssetLoaderParameters<BitmapFont> getParams() {

    BitmapFontLoader.BitmapFontParameter loaderParams = new BitmapFontLoader.BitmapFontParameter();
    loaderParams.minFilter = Texture.TextureFilter.Linear;
    loaderParams.magFilter = Texture.TextureFilter.Linear;
    return loaderParams;
  }
}
