package com.ziodyne.sometrpg.view.systems;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.Sprite;

public class SpriteBatchRenderer {
  private final Camera camera;
  private final SpriteBatch spriteBatch;
  private final ShaderProgram defaultShader = SpriteBatch.createDefaultShader();

  public SpriteBatchRenderer(Camera camera, SpriteBatch spriteBatch) {

    this.camera = camera;
    this.spriteBatch = spriteBatch;
  }

  public void render(Sprite sprite, Position position) {

    Color color = Color.WHITE;
    spriteBatch.setColor(color.r, color.g, color.b, sprite.getAlpha());

    float x = position.getX();
    float y = position.getY();
    Texture texture = sprite.getTexture();

    if (texture == null) {
      TextureRegion region = sprite.getRegion();
      spriteBatch.draw(region, x + sprite.getOffsetX(), y + sprite.getOffsetY(), region.getRegionWidth(), region.getRegionHeight());
    } else {
      float width = sprite.getWidth();
      float height = sprite.getHeight();

      spriteBatch.draw(texture, x, y, width, height);
    }
  }

  public void render(Sprite sprite, Position position, Shader shader, float delta) {
    shader.update(delta);
    spriteBatch.setShader(shader.getShader());
    render(sprite, position);
  }


  public void begin() {
    spriteBatch.setTransformMatrix(camera.view);
    spriteBatch.setProjectionMatrix(camera.projection);
    spriteBatch.begin();
  }

  public void end() {
    spriteBatch.end();
    spriteBatch.setShader(defaultShader);
  }
}
