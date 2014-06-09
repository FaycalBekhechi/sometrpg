package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.ViewportSpaceSprite;
import com.ziodyne.sometrpg.view.rendering.Sprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

public class ViewportSpaceSpriteRenderSystem extends EntitySystem {

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<ViewportSpaceSprite> spriteMapper;

  private final SpriteBatchRenderer batchRenderer;

  private final SpriteBatch batch;

  private final Viewport viewport;

  public ViewportSpaceSpriteRenderSystem(Viewport viewport) {
    super(Aspect.getAspectForAll(Position.class, ViewportSpaceSprite.class));

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
  protected void begin() {

    batchRenderer.begin();
  }

  @Override
  protected void end() {

    batchRenderer.end();
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity ent = entityImmutableBag.get(i);
      Position pos = positionMapper.get(ent);
      ViewportSpaceSprite spriteComponent = spriteMapper.get(ent);

      Vector2 posVec = new Vector2(pos.getX(), pos.getY());
      Sprite sprite = spriteComponent.getSprite();

      batchRenderer.render(sprite, posVec);
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
