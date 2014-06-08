package com.ziodyne.sometrpg.view.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.apache.commons.lang3.Validate;

public class Sprite {
  private TextureRegion region;
  private Texture texture;
  private Texture.TextureFilter minFilter;
  private Texture.TextureFilter magFiler;
  private float alpha = 1;
  private final float width;
  private final float height;
  private float offsetX = 0f;
  private float offsetY = 0f;

  public Sprite(Texture texture, float width, float height) {
    Validate.notNull(texture);

    this.texture = texture;
    this.width = width;
    this.height = height;
  }

  public Sprite(TextureRegion region, float width, float height) {
    Validate.notNull(region);

    this.width = width;
    this.height = height;
    this.region = region;
  }


  public void setRegion(TextureRegion region) {

    this.region = region;
  }

  public void setTexture(Texture texture) {

    this.texture = texture;
  }

  public void setMinFilter(Texture.TextureFilter minFilter) {

    this.minFilter = minFilter;
  }

  public void setMagFiler(Texture.TextureFilter magFiler) {

    this.magFiler = magFiler;
  }

  public void setAlpha(float alpha) {

    this.alpha = alpha;
  }

  public void setOffsetX(float offsetX) {

    this.offsetX = offsetX;
  }

  public void setOffsetY(float offsetY) {

    this.offsetY = offsetY;
  }

  public TextureRegion getRegion() {

    return region;
  }

  public Texture getTexture() {

    return texture;
  }

  public Texture.TextureFilter getMinFilter() {

    return minFilter;
  }

  public Texture.TextureFilter getMagFiler() {

    return magFiler;
  }

  public float getAlpha() {

    return alpha;
  }

  public float getWidth() {

    return width;
  }

  public float getHeight() {

    return height;
  }

  public float getOffsetX() {

    return offsetX;
  }

  public float getOffsetY() {

    return offsetY;
  }
}
