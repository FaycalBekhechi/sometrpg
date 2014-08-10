package com.ziodyne.sometrpg.view.systems;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.ViewportSpaceSprite;
import com.ziodyne.sometrpg.view.rendering.Sprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

public class ViewportSpaceSpriteRenderSystem extends IteratingSystem {

  private final SpriteBatchRenderer batchRenderer;

  private final SpriteBatch batch;

  private final Viewport viewport;

  public ViewportSpaceSpriteRenderSystem(Viewport viewport) {
    super(Family.getFamilyFor(Position.class, ViewportSpaceSprite.class));

    OrthographicCamera specialCam = new OrthographicCamera();
    specialCam.setToOrtho(false, viewport.getViewportWidth(), viewport.getViewportHeight());

    this.viewport = viewport;
    Camera cam = viewport.getCamera();
    if (cam instanceof OrthographicCamera) {
      OrthographicCamera orthoCam = (OrthographicCamera)cam;
      specialCam.zoom = orthoCam.zoom;
    }

    batch = new SpriteBatch();
    batchRenderer = new SpriteBatchRenderer(specialCam, batch);
  }

  @Override
  public void update(float deltaTime) {

    batchRenderer.begin();
    super.update(deltaTime);
    batchRenderer.end();
  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    Position pos = entity.getComponent(Position.class);
    ViewportSpaceSprite spriteComponent = entity.getComponent(ViewportSpaceSprite.class);

    Vector2 posVec = new Vector2(pos.getX(), pos.getY());
    Sprite sprite = spriteComponent.getSprite();

    batchRenderer.render(sprite, posVec);
  }
}
