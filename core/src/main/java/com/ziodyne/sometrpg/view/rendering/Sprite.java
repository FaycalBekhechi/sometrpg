package com.ziodyne.sometrpg.view.rendering;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.apache.commons.lang3.Validate;

public class Sprite {
  private TextureRegion region;
  private Texture texture;
  private Texture.TextureFilter minFilter;
  private Texture.TextureFilter magFiler;
  private float scale = 1f;
  private float alpha = 1f;
  private final float width;
  private final float height;
  private float offsetX = 0f;
  private float offsetY = 0f;
  private float rotation = 0f;
  private float originX;
  private float originY;

  public Sprite(Texture texture, float width, float height) {
    Validate.notNull(texture);

    this.texture = texture;
    this.width = width;
    this.height = height;
    this.originX = width / 2;
    this.originY = height /2;
  }

  public Sprite(TextureRegion region, float width, float height) {
    Validate.notNull(region);

    this.width = width;
    this.height = height;
    this.region = region;
    this.originX = width / 2;
    this.originY = height /2;
  }

  public float getRotation() {

    return rotation;
  }

  public void setRotation(float rotation) {

    this.rotation = rotation;
  }

  public float getScale() {

    return scale;
  }

  public void setScale(float scale) {

    this.scale = scale;
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

  public float getOriginX() {

    return originX;
  }

  public void setOriginX(float originX) {

    this.originX = originX;
  }

  public float getOriginY() {

    return originY;
  }

  public void setOriginY(float originY) {

    this.originY = originY;
  }
}
