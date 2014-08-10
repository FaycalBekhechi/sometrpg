package com.ziodyne.sometrpg.view.systems;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.ashley.utils.ImmutableIntMap;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.google.common.collect.Lists;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
import com.ziodyne.sometrpg.logging.GdxLogger;
import com.ziodyne.sometrpg.logging.Logger;
import com.ziodyne.sometrpg.view.components.Position;
import com.ziodyne.sometrpg.view.components.Shader;
import com.ziodyne.sometrpg.view.components.SpriteComponent;
import com.ziodyne.sometrpg.view.rendering.Sprite;
import com.ziodyne.sometrpg.view.rendering.SpriteBatchRenderer;

public class SpriteRenderSystem extends IteratingSystem {
  private static final Logger LOG = new GdxLogger(SpriteRenderSystem.class);

  private final SpriteBatchRenderer spriteBatchRenderer;
  private List<Entity> zSortedEntities;

  public interface Factory {
    public SpriteRenderSystem create(OrthographicCamera camera);
  }

  @AssistedInject
  @SuppressWarnings("unchecked")
  SpriteRenderSystem(@Assisted OrthographicCamera camera, SpriteBatch spriteBatch) {

    super(Family.getFamilyFor(Position.class, SpriteComponent.class));
    spriteBatchRenderer = new SpriteBatchRenderer(camera, spriteBatch);
  }

  @Override
  public void update(float deltaTime) {

    spriteBatchRenderer.begin();
    zSortedEntities.stream()
      .forEach(e -> this.processEntity(e, deltaTime));
    spriteBatchRenderer.end();
  }

  @Override
  public void addedToEngine(Engine engine) {

    super.addedToEngine(engine);

    ImmutableIntMap<Entity> familyEntities = engine.getEntitiesFor(Family.getFamilyFor(Position.class,
      SpriteComponent.class));
    List<Entity> famEntitiesList = new ArrayList<>();
    for (Entity entity : familyEntities.values()) {
      famEntitiesList.add(entity);
    }

    zSortedEntities = famEntitiesList.stream()
      .sorted(byZIndex())
      .collect(Collectors.toList());

  }

  @Override
  public void processEntity(Entity entity, float deltaTime) {

    Position pos = entity.getComponent(Position.class);
    Vector2 position = new Vector2(pos.getX(), pos.getY());

    SpriteComponent spriteComponent = entity.getComponent(SpriteComponent.class);
    Sprite sprite = spriteComponent.getSprite();

    if (entity.hasComponent(Shader.class)) {
      Shader shaderComponent = entity.getComponent(Shader.class);
      shaderComponent.update(deltaTime);
      spriteBatchRenderer.render(sprite, position, shaderComponent.getShader());
    } else {
      spriteBatchRenderer.render(sprite, position);
    }
  }

  private static Comparator<Entity> byZIndex() {

    return (o1, o2) -> o1.getComponent(SpriteComponent.class).getzIndex() - o2.getComponent(
      SpriteComponent.class).getzIndex();
  }

}

