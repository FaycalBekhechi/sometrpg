package com.ziodyne.sometrpg.view.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Mapper;
import com.artemis.utils.ImmutableBag;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.VoidSprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

public class VoidSpriteRenderSystem extends EntitySystem {
  private final SpriteBatchRenderer renderer;

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<VoidSprite> spriteMapper;

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

    super(Aspect.getAspectForAll(Position.class, VoidSprite.class));

    renderer = new SpriteBatchRenderer(camera, batch);
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entityImmutableBag) {
    for (int i = 0; i < entityImmutableBag.size(); i++) {
      Entity ent = entityImmutableBag.get(i);

      VoidSprite spriteComponent = spriteMapper.get(ent);
      Position pos = positionMapper.get(ent);

      renderer.render(spriteComponent.getSprite(), new Vector2(pos.getX(), pos.getY()));
    }
  }

  @Override
  protected boolean checkProcessing() {

    return true;
  }
}
