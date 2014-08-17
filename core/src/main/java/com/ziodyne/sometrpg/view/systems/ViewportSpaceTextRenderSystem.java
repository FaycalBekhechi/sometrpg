package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.StaticShader;
import com.ziodyne.sometrpg.view.components.Text;
import com.ziodyne.sometrpg.view.components.ViewportPosition;

public class ViewportSpaceTextRenderSystem extends IteratingSystem {
  private final Viewport viewport;
  private final OrthographicCamera camera;
  private final SpriteBatch batch;

  public interface Factory {
    ViewportSpaceTextRenderSystem create(Viewport viewport);
  }

  @AssistedInject
  ViewportSpaceTextRenderSystem(SpriteBatch batch, @Assisted Viewport viewport) {

    super(Family.getFamilyFor(Text.class, ViewportPosition.class));
    this.batch = batch;
    this.viewport = viewport;
    this.camera = new OrthographicCamera();
    this.camera.setToOrtho(false, viewport.getViewportWidth(), viewport.getViewportHeight());
  }

  @Override
  public void update(float deltaTime) {
    Matrix4 originalProj = batch.getProjectionMatrix();
    Matrix4 originalTrans = batch.getTransformMatrix();

    batch.setTransformMatrix(camera.view);
    batch.setProjectionMatrix(camera.projection);

    batch.begin();
    super.update(deltaTime);
    batch.end();

    batch.setTransformMatrix(originalTrans);
    batch.setProjectionMatrix(originalProj);
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {
    ViewportPosition posCmp = entity.getComponent(ViewportPosition.class);
    Vector2 pos = posCmp.getPosition();
    Text text = entity.getComponent(Text.class);
    BitmapFont font = text.getFont();

    if (entity.hasComponent(StaticShader.class)) {
      batch.setShader(entity.getComponent(StaticShader.class).getShader());
      font.draw(batch, text.getCharacters(), pos.x, pos.y);
      batch.setShader(null);
    } else {
      font.draw(batch, text.getCharacters(), pos.x, pos.y);
    }
  }
}
