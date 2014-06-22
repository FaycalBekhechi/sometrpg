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

    Color color = Color.WHITE;
    spriteBatch.setColor(color.r, color.g, color.b, sprite.getAlpha());

    float x = position.x;
    float y = position.y;
    Texture texture = sprite.getTexture();

    if (texture == null) {
      TextureRegion region = sprite.getRegion();
      spriteBatch.draw(region, x + sprite.getOffsetX(), y + sprite.getOffsetY(), region.getRegionWidth(), region.getRegionHeight());
    } else {
      float scale = sprite.getScale();
      float width = sprite.getWidth() * scale;
      float height = sprite.getHeight() * scale;

      spriteBatch.draw(texture, x, y, width, height);
    }
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
    spriteBatch.setShader(defaultShader);
    spriteBatch.setTransformMatrix(originalTransform);
    spriteBatch.setProjectionMatrix(originalProjection);
  }
}