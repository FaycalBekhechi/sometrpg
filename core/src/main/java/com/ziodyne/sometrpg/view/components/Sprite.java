package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.apache.commons.lang3.Validate;

public class Sprite extends Component {
  private TextureRegion region;
  private Texture texture;
  private Texture.TextureFilter minFilter;
  private Texture.TextureFilter magFiler;
  private float alpha = 1;
  private final float width;
  private final float height;

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

  public float getAlpha() {
    return alpha;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
  }

  public TextureRegion getRegion() {
    return region;
  }

  public Texture getTexture() {
    return texture;
  }

  public float getWidth() {
    return width;
  }

  public float getHeight() {
    return height;
  }

  public Texture.TextureFilter getMinFilter() {
    return minFilter;
  }

  public void setMinFilter(Texture.TextureFilter minFilter) {
    this.minFilter = minFilter;
  }

  public Texture.TextureFilter getMagFiler() {
    return magFiler;
  }

  public void setMagFiler(Texture.TextureFilter magFiler) {
    this.magFiler = magFiler;
  }
}
