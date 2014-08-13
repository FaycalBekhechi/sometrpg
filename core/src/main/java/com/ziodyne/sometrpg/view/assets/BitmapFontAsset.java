package com.ziodyne.sometrpg.view.assets;

import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;

public class BitmapFontAsset extends Asset<BitmapFont> {
  public BitmapFontAsset() {
    super("bitmap_font", BitmapFont.class);
  }

  @Override
  public AssetLoaderParameters<BitmapFont> getParams() {

    FreetypeFontLoader.FreeTypeFontLoaderParameter loaderParams = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
    loaderParams.fontFileName = getPath();
    return loaderParams;
  }
}
