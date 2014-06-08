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
import com.google.common.collect.HashMultimap;
import com.google.common.collect.SetMultimap;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.components.VoidSprite;
import com.ziodyne.sometrpg.view.graphics.SpriteLayer;
import com.ziodyne.sometrpg.view.rendering.Sprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

import java.util.Set;

public class SpriteRenderSystem extends EntitySystem {

  @Mapper
  private ComponentMapper<Position> positionMapper;

  @Mapper
  private ComponentMapper<SpriteComponent> spriteMapper;

  @Mapper
  private ComponentMapper<Shader> shaderMapper;

  private final SpriteBatchRenderer spriteBatchRenderer;

  public interface Factory {
    public SpriteRenderSystem create(OrthographicCamera camera);
  }

  @AssistedInject
  @SuppressWarnings("unchecked")
  SpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch spriteBatch) {
    super(Aspect.getAspectForAll(Position.class, SpriteComponent.class));
    spriteBatchRenderer = new SpriteBatchRenderer(camera, spriteBatch);
  }

  @Override
  protected void initialize() {
  }

  @Override
  protected void begin() {
    spriteBatchRenderer.begin();
  }

  @Override
  protected void end() {
    spriteBatchRenderer.end();
  }

  @Override
  protected void processEntities(ImmutableBag<Entity> entites) {
    // Group entities by their layer
    SetMultimap<SpriteLayer, Entity> entitiesByLayer = HashMultimap.create();
    for (int i = 0; i < entites.size(); i++) {
      Entity entity = entites.get(i);
      SpriteComponent spriteComponentComponent = spriteMapper.get(entity);

      SpriteLayer layer = spriteComponentComponent.getLayer();
      if (layer != null) {
        Set<Entity> entitiesForLayer = entitiesByLayer.get(layer);
        entitiesForLayer.add(entity);
      }
    }

    // Render each layer in declaration order
    for (SpriteLayer layer : SpriteLayer.values()) {
      for (Entity entity : entitiesByLayer.get(layer)) {
        render(entity);
      }
    }
  }

  private void render(Entity entity) {
    Position pos = positionMapper.get(entity);
    Vector2 position = new Vector2(pos.getX(), pos.getY());

    SpriteComponent spriteComponent = spriteMapper.get(entity);
    Sprite sprite = spriteComponent.getSprite();

    Shader shaderComponent = shaderMapper.getSafe(entity);
    if (shaderComponent != null) {
      shaderComponent.update(world.getDelta());
      spriteBatchRenderer.render(sprite, position, shaderComponent.getShader());
    } else {
      spriteBatchRenderer.render(sprite, position);
    }
  }

  @Override
  protected boolean checkProcessing() {
    return true;
  }
}
