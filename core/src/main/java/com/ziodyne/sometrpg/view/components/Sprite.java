package com.ziodyne.sometrpg.view.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Texture;

public class Sprite extends Component {
  private Texture texture;
  private Texture.TextureFilter minFilter;
  private Texture.TextureFilter magFiler;
  private float alpha = 1;
  private final float width;
  private final float height;

  public Sprite(String path) {
    texture = new Texture(path);
    width = texture.getWidth();
    height = texture.getHeight();
  }

  public Sprite(String path, float width, float height) {
    texture = new Texture(path);
    this.width = width;
    this.height = height;
  }

  public Sprite(String path, float width, float height, float alpha) {
    this(path, width, height);
    this.alpha = alpha;
  }

  public float getAlpha() {
    return alpha;
  }

  public void setAlpha(float alpha) {
    this.alpha = alpha;
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
