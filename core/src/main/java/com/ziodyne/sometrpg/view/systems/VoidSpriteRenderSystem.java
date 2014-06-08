package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Sprite;
import com.ziodyne.sometrpg.view.components.VoidSprite;

public class VoidSpriteRenderSystem extends EntitySystem {
  private final SpriteBatchRenderer renderer;

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<Sprite> spriteMapper;

  public interface Factory {
    public VoidSpriteRenderSystem create(OrthographicCamera  camera);
  }

  @Override
  protected void begin() {

    renderer.begin();
  }

  @Override
  protected void end() {

    renderer.end();
  }

  @AssistedInject
  @SuppressWarnings("unchecked")
  public VoidSpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch batch) {

    super(Aspect.getAspectForAll(Sprite.class, Position.class, VoidSprite.class));

    renderer = new SpriteBatchRenderer(camera, batch);
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity ent = entityImmutableBag.get(i);

      Sprite sprite = spriteMapper.get(ent);
      Position pos = positionMapper.get(ent);

      renderer.render(sprite, pos);
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
