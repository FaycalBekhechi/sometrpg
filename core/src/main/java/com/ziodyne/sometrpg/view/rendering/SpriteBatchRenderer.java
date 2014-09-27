package com.ziodyne.sometrpg.view.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;

public class SpriteBatchRenderer {
  private final Camera camera;
  private final SpriteBatch spriteBatch;
  private final ShaderProgram defaultShader = SpriteBatch.createDefaultShader();

  private Matrix4 originalTransform;
  private Matrix4 originalProjection;

  public SpriteBatchRenderer(Camera camera, SpriteBatch spriteBatch) {

    this.camera = camera;
    this.spriteBatch = spriteBatch;
  }

  public void render(Sprite sprite, Vector2 position) {

    float x = position.x;
    float y = position.y;
    Texture texture = sprite.getTexture();

    if (sprite.getAlpha() < 1) {
      spriteBatch.setColor(1.0f, 1.0f, 1.0f, sprite.getAlpha());
    }

    if (texture == null) {
      TextureRegion region = sprite.getRegion();
      spriteBatch.draw(region, x + sprite.getOffsetX(), y + sprite.getOffsetY(), sprite.getOriginX(), sprite.getOriginY(),
        region.getRegionWidth(), region.getRegionHeight(),
        sprite.getScale(), sprite.getScale(), sprite.getRotation());
    } else {
      float scale = sprite.getScale();
      float width = sprite.getWidth() * scale;
      float height = sprite.getHeight() * scale;

      spriteBatch.draw(texture, x, y, width, height);
    }

    spriteBatch.setColor(Color.WHITE);
  }

  public void render(Sprite sprite, Vector2 position, ShaderProgram shader) {
    spriteBatch.setShader(shader);
    render(sprite, position);
  }


  public void begin() {
    originalProjection = spriteBatch.getProjectionMatrix();
    originalTransform = spriteBatch.getTransformMatrix();

    spriteBatch.setTransformMatrix(camera.view);
    spriteBatch.setProjectionMatrix(camera.projection);
    spriteBatch.begin();
  }

  public void end() {
    spriteBatch.end();
    spriteBatch.setShader(null);
    spriteBatch.setTransformMatrix(originalTransform);
    spriteBatch.setProjectionMatrix(originalProjection);
  }
}
