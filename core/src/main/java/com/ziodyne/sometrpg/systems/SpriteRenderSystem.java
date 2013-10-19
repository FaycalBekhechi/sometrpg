package com.ziodyne.sometrpg.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ziodyne.sometrpg.components.Position;
import com.ziodyne.sometrpg.components.Sprite;

public class SpriteRenderSystem extends EntitySystem {

  private final OrthographicCamera camera;
  private final SpriteBatch batch;

  @Mapper
  ComponentMapper<Position> positionMapper;

  @Mapper
  ComponentMapper<Sprite> spriteMapper;

  @SuppressWarnings("unchecked")
  public SpriteRenderSystem(OrthographicCamera camera, SpriteBatch spriteBatch) {
    super(Aspect.getAspectForAll(Position.class, Sprite.class));
    this.camera = camera;
    this.batch = spriteBatch;
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void begin() {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
  }

  @Override
  protected void end() {
    batch.end();
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entites) {
    for (int i = 0; i < entites.size(); i++) {
      Entity entity = entites.get(i);

      Position pos = positionMapper.get(entity);
      Sprite sprite = spriteMapper.get(entity);

      batch.setColor(sprite.color);
      batch.draw(sprite.texture, pos.getX(), pos.getY());
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
